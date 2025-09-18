package com.example.here4u

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load ExercisesFragment when the app starts
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ExercisesFragment())
            .commit()
    }
}
