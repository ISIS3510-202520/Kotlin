package com.example.here4u.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.here4u.view.exercises.ExercisesActivity
import com.example.here4u.view.emotions.IdentifyingEmotions
import com.example.here4u.R
import com.example.here4u.databinding.ActivityHomeBinding
import com.example.here4u.view.recap.TrendsFragment
import com.google.android.material.button.MaterialButton
import com.example.here4u.view.emergency.Emergency

import com.example.here4u.viewmodel.EmotionsViewModel
import com.example.here4u.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class home : AppCompatActivity() { // Note: Class names in Kotlin usually start with an uppercase letter, like 'Home'
    private val viewModel: HomeViewModel by viewModels()


    private lateinit var binding: ActivityHomeBinding
    @SuppressLint("MissingInflatedId")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // This is for edge-to-edge display, not directly related to the crash
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

         // This should link to your 'activity_home.xml' layout

        // Botón Daily Exercises

        binding.btnExercises.setOnClickListener {
            val intent = Intent(this, ExercisesActivity::class.java)
            startActivity(intent)



        }



        binding.btnRegisterMood.setOnClickListener {
            val intent = Intent(
                this,
                IdentifyingEmotions::class.java
            ) // Creates an Intent to go to 'home' Activity
            startActivity(intent)
        }


        val trendsButton = findViewById<Button>(R.id.btnRecap)



        binding.btnRecap.setOnClickListener {
           binding.fragmentContainer.visibility = View.VISIBLE // show container
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TrendsFragment())
                .addToBackStack(null) // allows back navigation
                .commit()
        }
        binding.btnEmergency.setOnClickListener {
            val intent = Intent(this, Emergency::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moodText.collect { text ->
                    binding.btnRegisterMood.text = text
                }
            }

    }}
    // Fragment
    override fun onResume() {
        super.onResume()
        viewModel.refreshMoodText() // calcula y publica el texto
    }






}