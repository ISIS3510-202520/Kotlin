package com.example.here4u.view.emergency

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Network
import android.net.NetworkRequest
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.here4u.databinding.ActivityEmergencyBinding
import com.example.here4u.utils.NetworkUtils
import com.example.here4u.viewmodel.EmergencyContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import com.example.here4u.data.mappers.toRemote
import android.net.ConnectivityManager
import android.content.Context

@AndroidEntryPoint
class Emergency : AppCompatActivity() {

    private lateinit var binding: ActivityEmergencyBinding
    private val viewModel: EmergencyContactsViewModel by viewModels()
    private lateinit var adapter: ContactsAdapter

    private var pendingEmergency = false

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                if (isLocationEnabled()) {

                    lifecycleScope.launch {
                        val ans = viewModel.createEmergency()
                        if (ans) {
                            Toast.makeText(this@Emergency, "Email Sent!", LENGTH_SHORT).show()
                        }
                        pendingEmergency = false
                    }

                }
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ContactsAdapter()

        binding.rvContacts.layoutManager = GridLayoutManager(this, 2)
        binding.rvContacts.adapter = adapter
        val online = NetworkUtils.isNetworkAvailable(this)
        binding.btnAddContact.alpha = if (online) 1f else 0.5f
        binding.btnAddContact.setOnClickListener {
            val online = NetworkUtils.isNetworkAvailable(this)
            if (online) {
                startActivity(Intent(this, CreateContact::class.java))
            } else {
                Toast.makeText(
                    this,
                    "You can't add emergency contacts without internet.",
                    Toast.LENGTH_SHORT
                ).apply {
                    setGravity(android.view.Gravity.CENTER, 0, 0)
                }.show()
            }
        }

        binding.btnCall.setOnClickListener {
            checkPermission()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        viewModel.loadLocalContacts()
        observeContacts()
    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            if (NetworkUtils.isNetworkAvailable(this@Emergency)) {
                viewModel.syncPendingContacts()
                viewModel.loadContacts()
                viewModel.loadLocalContacts()

            } else {
                viewModel.loadLocalContacts()
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun checkPermission() {
        if (NetworkUtils.isNetworkAvailable(this)){
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {

                    if (isLocationEnabled()) {
                        lifecycleScope.launch {
                            val res = viewModel.createEmergency()
                            if (res) {
                                Toast.makeText(this@Emergency, "Email Sent!", LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        showGpsDialog()
                    }
                }

                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    AlertDialog.Builder(this)
                        .setTitle("Permiso de ubicación necesario")
                        .setMessage("La app necesita tu ubicación para poder enviar tu emergencia.")
                        .setPositiveButton("Aceptar") { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                        .setNegativeButton("No, gracias") { dialog, _ -> dialog.dismiss() }
                        .show()
                }

                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }

        else{

            showNoInternetDialog()

        }}

    private fun isLocationEnabled(): Boolean {
        val lm = getSystemService(LOCATION_SERVICE) as android.location.LocationManager
        return androidx.core.location.LocationManagerCompat.isLocationEnabled(lm)
    }

    private fun openLocationSettings() {
        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    private fun showGpsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Ubicación desactivada")
            .setMessage("Activa el GPS para poder enviar tu ubicación en caso de emergencia.")
            .setPositiveButton("Abrir ajustes") { _, _ -> openLocationSettings() }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
                pendingEmergency = false
            }
            .show()
    }

    private fun showNoInternetDialog(){
        val contacts = viewModel.localcontacts.value
        if (contacts.isNullOrEmpty()){
            AlertDialog.Builder(this)
                .setTitle("You have No internet connection to send the emails and no contacts saved in the local database")
                .setMessage("Would you rather talk with line 106 with 24/7 psychological help?")
                .setPositiveButton("yes") {  dialog, _ ->
                    openDialer("106")
                    dialog.dismiss() }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                    pendingEmergency = false
                }
                .show()}
        else{
            val firstContact = contacts.first()
            AlertDialog.Builder(this)
                .setTitle("You have No internet connection to send the emails ")
                .setMessage("Would you rather talk with your first emergency contact (${firstContact.name})?")
                .setPositiveButton("yes") {  dialog, _ ->
                    openDialer(firstContact.phone)
                    dialog.dismiss() }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                    pendingEmergency = false
                }
                .show()

        }

    }

    private fun openDialer(number: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")

        }
        startActivity(intent)

    }
    private fun observeContacts() {

            viewModel.localcontacts.observe(this) { contacts ->
                val remotes = contacts.map { it.toRemote() }
                adapter.updateData(remotes)
            }
            }

    private val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            runOnUiThread {
                binding.btnAddContact.isEnabled = true
                binding.btnAddContact.alpha = 1f
            }
            lifecycleScope.launch {
                viewModel.syncPendingContacts()
                viewModel.loadContacts()
                viewModel.loadLocalContacts()
            }

        }

        override fun onLost(network: Network) {
            runOnUiThread {
                binding.btnAddContact.alpha = 0.5f

            }
            lifecycleScope.launch {
                viewModel.loadLocalContacts()
            }
        }}

    override fun onStart() {
        super.onStart()
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}