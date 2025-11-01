package com.example.here4u.view.emergency

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.here4u.R
import com.example.here4u.data.local.entity.EmergencyContactEntity
import com.example.here4u.viewmodel.EmergencyContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateContact : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etRelationship: EditText
    private lateinit var etNumber: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnBack: Button

    private val emergencyContactsViewModel: EmergencyContactsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contact)

        etName = findViewById(R.id.etName)
        etRelationship = findViewById(R.id.etRelationship)
        etNumber = findViewById(R.id.etNumber)
        etEmail = findViewById(R.id.etEmail)
        btnAdd = findViewById(R.id.btnAdd)
        btnBack = findViewById(R.id.btnBack)

        btnAdd.setOnClickListener { saveContact() }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun saveContact() {
        val name = etName.text.toString().trim()
        val relationship = etRelationship.text.toString().trim()
        val number = etNumber.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (name.isEmpty() || relationship.isEmpty() || number.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val contactEntity = EmergencyContactEntity(
            name = name,
            relation = relationship,
            phone = number,
            email = email
        )

        lifecycleScope.launch {
            val success = emergencyContactsViewModel.addEmergencyContact(contactEntity)
            if (success) {
                Toast.makeText(this@CreateContact, " Contact saved", Toast.LENGTH_SHORT).show()
                clearFields()
            } else {
                Toast.makeText(this@CreateContact, " Error saving contact", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        etName.text.clear()
        etRelationship.text.clear()
        etNumber.text.clear()
        etEmail.text.clear()
    }
}