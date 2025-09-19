package com.example.here4u

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.here4u.databinding.ActivityHomeBinding
import com.example.here4u.databinding.ActivityIdentifyingEmotionsBinding
import com.example.here4u.databinding.ActivityJournalingBinding

class home : AppCompatActivity() { // Note: Class names in Kotlin usually start with an uppercase letter, like 'Home'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // This is for edge-to-edge display, not directly related to the crash
        setContentView(R.layout.activity_home)
        val registermood = findViewById<Button>(R.id.btnRegisterMood)

        registermood.setOnClickListener {
            val intent = Intent(this, IdentifyingEmotions::class.java) // Creates an Intent to go to 'home' Activity
            startActivity(intent)
        }



    }
}