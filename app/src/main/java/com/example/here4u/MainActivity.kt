package com.example.here4u

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Instead of showing activity_home directly:
        val intent = Intent(this, home::class.java)
        startActivity(intent)
        finish()

    }
}
