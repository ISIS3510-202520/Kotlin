package com.example.here4u.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
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
import com.example.here4u.view.profile.ProfileActivity
import com.example.here4u.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.view.LayoutInflater
import android.widget.LinearLayout
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class home : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnExercises.setOnClickListener {
            startActivity(Intent(this, ExercisesActivity::class.java))
        }

        binding.btnRegisterMood.setOnClickListener {
            startActivity(Intent(this, IdentifyingEmotions::class.java))
        }

        binding.btnRecap.setOnClickListener {
            binding.fragmentContainer.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TrendsFragment())
                .addToBackStack(null)
                .commit()
        }
        val  btnEmergency = findViewById<Button>(R.id.btnEmergency)
        btnEmergency.setOnClickListener {

            val intent = Intent(this, Emergency::class.java)
            startActivity(intent)
        }


        binding.btnEmergency.setOnClickListener {
            startActivity(Intent(this, Emergency::class.java))
        }

        // --- Collect both mood text and streak from ViewModel ---
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.moodText.collect { text ->
                        binding.btnRegisterMood.text = text
                    }
                }

                launch {
                    viewModel.streak.collect { current ->
                        binding.tvStreak.text = "🔥 Streak\n${current} Days"
                    }
                }
            }
        }
        binding.userboton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))

        }
        // --- Mostrar últimos 5 journals ---
        val container = findViewById<LinearLayout>(R.id.containerJournals)
        val inflater = LayoutInflater.from(this)
        viewModel.lastFive.observe(this) { journals ->
                container.removeAllViews()

                if (journals.isEmpty()) {
                    val emptyText = TextView(this@home).apply {
                        text = "You haven’t written any journal yet."
                        textSize = 14f
                        setTextColor(android.graphics.Color.GRAY)
                        setPadding(16, 16, 16, 16)
                    }
                    container.addView(emptyText)
                } else {
                    journals.take(5).forEach { journal ->
                        val itemView = inflater.inflate(
                            R.layout.item_journal_preview,
                            container,
                            false
                        )

                        val desc = itemView.findViewById<TextView>(R.id.tvJournalDescription)
                        val date = itemView.findViewById<TextView>(R.id.tvJournalDate)

                        desc.text = if (journal.description.length > 80)
                            journal.description.take(80) + "..."
                        else
                            journal.description

                        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        date.text = sdf.format(journal.createdAt.toDate())


                        itemView.setOnClickListener {
                            AlertDialog.Builder(this@home)
                                .setTitle("Journal Entry")
                                .setMessage(journal.description)
                                .setPositiveButton("Close", null)
                                .show()
                        }

                        container.addView(itemView)
                    }
                }
            }







    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshMoodText()
        viewModel.refreshUserStreak()
    }
}
