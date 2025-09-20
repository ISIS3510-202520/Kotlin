// ExercisesActivity.kt
package com.example.here4u

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ExercisesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises) // This will host the fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_exercises, ExercisesFragment())
            .commit()
    }
}
