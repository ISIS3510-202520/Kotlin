package com.example.here4u.view.emergency

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
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

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) @androidx.annotation.RequiresPermission(
            allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION]
        ) { granted ->
            if (granted) {

                viewModel.createEmergency()

            }
            else {Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar adapter

        adapter= ContactsAdapter()


        // Configurar RecyclerView
        binding.rvContacts.layoutManager = GridLayoutManager(this,2)
        binding.rvContacts.adapter = adapter

        // Botón para agregar contacto
        binding.btnAddContact.setOnClickListener {
            startActivity(Intent(this, CreateContact::class.java))
        }

        // Botón para notificar a todos
        binding.btnCall.setOnClickListener {

            checkPermission()
            // Aquí va tu lógica para notificar a todos
        }

        // Botón de regreso
        binding.btnBack.setOnClickListener {
            finish()
        }


    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    override fun onResume() {
        super.onResume()
        // Actualizar lista de contactos al volver de CreateContact
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contacts.collect { list ->
                    adapter.updateData(list)
                }
            }
        }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.createEmergency()
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
}
