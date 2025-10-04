package com.example.here4u.view.emergency

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.here4u.databinding.ActivityEmergencyBinding
import com.example.here4u.viewmodel.EmergencyContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Emergency : AppCompatActivity() {

    private lateinit var binding: ActivityEmergencyBinding
    private val viewModel: EmergencyContactsViewModel by viewModels()
    private lateinit var adapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 Inicializar adapter
        adapter = ContactsAdapter()

        // 🔹 Configurar RecyclerView
        binding.rvContacts.layoutManager = GridLayoutManager(this, 2)
        binding.rvContacts.adapter = adapter

        // 🔹 Botón para agregar contacto
        binding.btnAddContact.setOnClickListener {
            startActivity(Intent(this, CreateContact::class.java))
        }

        // 🔹 Mensaje de prueba (puedes reemplazarlo por la ubicación real)
        val locationMessage = "🚨 ¡Alerta! Se detectó una emergencia en tu ubicación."

        // 🔹 Botón para notificar a todos los contactos
        binding.btnCall.setOnClickListener {
            try {
                viewModel.sendMail(locationMessage)
            } catch (e: Exception) {
                android.util.Log.e("EmergencyActivity", "❌ Error al enviar alerta: ${e.message}", e)
            }
        }

        // 🔹 Botón de regreso
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    override fun onResume() {
        super.onResume()

        // 🔹 Actualizar lista de contactos al volver de CreateContact
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contacts.collect { list ->
                    android.util.Log.d("EmergencyActivity", "📋 Se recibieron ${list.size} contactos.")
                    adapter.updateData(list)
                }
            }
        }
    }
}
