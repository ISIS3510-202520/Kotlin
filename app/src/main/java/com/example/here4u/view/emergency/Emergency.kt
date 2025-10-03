package com.example.here4u.view.emergency

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.here4u.R

class Emergency : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)


        val nameField = findViewById<Button>(R.id.btnAddContact)
        nameField.setOnClickListener {
            val intent = Intent(this, CreateContact::class.java)
            startActivity(intent)
        }
    }
}
