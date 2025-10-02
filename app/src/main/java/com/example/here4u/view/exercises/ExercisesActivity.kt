package com.example.here4u.view.exercises

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.here4u.R

class ExercisesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises) // This will host the fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_exercises, ExercisesFragment())
            .commit()
    }
}