package com.example.here4u

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // This links to your XML layout

        val loginButton = findViewById<Button>(R.id.loginButton) // Finds the button by its ID

        loginButton.setOnClickListener {
            val intent = Intent(this, home::class.java) // Creates an Intent to go to 'home' Activity
            startActivity(intent) // Starts the 'home' Activity
        }
    }
}