package com.example.here4u.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.example.here4u.view.exercises.ExercisesActivity
import com.example.here4u.view.emotions.IdentifyingEmotions
import com.example.here4u.R
import com.example.here4u.view.recap.TrendsFragment
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.here4u.viewmodel.JournalingViewModel

@AndroidEntryPoint
class home : AppCompatActivity() { // Note: Class names in Kotlin usually start with an uppercase letter, like 'Home'

    private lateinit var journalingViewModel: JournalingViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // This is for edge-to-edge display, not directly related to the crash


        setContentView(R.layout.activity_home) // This should link to your 'activity_home.xml' layout

        // ✅ Setup ViewModel
        journalingViewModel = ViewModelProvider(this)[JournalingViewModel::class.java]

        // ✅ Observe streak
        val tvStreak = findViewById<TextView>(R.id.tvStreak)
        journalingViewModel.streak.observe(this) { days ->
            tvStreak.text = if (days > 0) {
                "🔥 Streak\n$days Days"
            } else {
                "No streak yet"
            }
        }

        // Botón Daily Exercises
        val btnExercises = findViewById<MaterialButton>(R.id.btnExercises)
        btnExercises.setOnClickListener {
            val intent = Intent(this, ExercisesActivity::class.java)
            startActivity(intent)



        }

        val registermood = findViewById<Button>(R.id.btnRegisterMood)

        registermood.setOnClickListener {
            val intent = Intent(
                this,
                IdentifyingEmotions::class.java
            ) // Creates an Intent to go to 'home' Activity
            startActivity(intent)
        }


        val trendsButton = findViewById<Button>(R.id.btnRecap)

        val fragmentContainer = findViewById<FragmentContainerView>(R.id.fragmentContainer)

        trendsButton.setOnClickListener {
            fragmentContainer.visibility = View.VISIBLE // show container
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TrendsFragment())
                .addToBackStack(null) // allows back navigation
                .commit()
        }


    }

}