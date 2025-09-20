package com.example.here4u

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import android.content.Intent
import androidx.fragment.app.commit
import androidx.fragment.app.replace
class home : AppCompatActivity() { // Note: Class names in Kotlin usually start with an uppercase letter, like 'Home'
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // This is for edge-to-edge display, not directly related to the crash
        setContentView(R.layout.activity_home) // This should link to your 'activity_home.xml' layout

        // Botón Daily Exercises
        val btnExercises = findViewById<MaterialButton>(R.id.btnExercises)
        btnExercises.setOnClickListener {
            val intent = Intent(this, ExercisesActivity::class.java)
            startActivity(intent)
        }
        }

    }
