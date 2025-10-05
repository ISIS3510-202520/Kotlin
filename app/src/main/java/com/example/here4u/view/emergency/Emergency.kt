package com.example.here4u.view.emergency

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.here4u.databinding.ActivityEmergencyBinding
import com.example.here4u.viewmodel.EmergencyContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Emergency : AppCompatActivity() {

    private lateinit var binding: ActivityEmergencyBinding
    private val viewModel: EmergencyContactsViewModel by viewModels()
    private lateinit var adapter: ContactsAdapter

    private var pendingEmergency = false

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) @androidx.annotation.RequiresPermission(
            allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION]
        ) { granted ->
            if (granted) {

                if (isLocationEnabled()){
                    val ans = viewModel.createEmergency()
                    if (ans){
                        Toast.makeText(this,"Email Sent!",LENGTH_SHORT).show()
                    }

                    pendingEmergency = false}

            }
            else {Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = ContactsAdapter()


        binding.rvContacts.layoutManager = GridLayoutManager(this, 2)
        binding.rvContacts.adapter = adapter


        binding.btnAddContact.setOnClickListener {
            startActivity(Intent(this, CreateContact::class.java))
        }


        binding.btnCall.setOnClickListener {
            checkPermission()



        }


        binding.btnBack.setOnClickListener {
            finish()
        }


    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    override fun onResume() {
        super.onResume()


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contacts.collect { list ->
                    android.util.Log.d("EmergencyActivity", "Se recibieron ${list.size} contactos.")
                    adapter.updateData(list)
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {

                if (isLocationEnabled()){
                val res = viewModel.createEmergency()
                    if (res){
                        Toast.makeText(this,"Email Sent!",LENGTH_SHORT).show()
                    }
                }
                else {
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

}
