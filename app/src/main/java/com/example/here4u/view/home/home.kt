package com.example.here4u.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.here4u.view.exercises.ExercisesActivity
import com.example.here4u.view.emotions.IdentifyingEmotions
import com.example.here4u.R
import com.example.here4u.databinding.ActivityHomeBinding
import com.example.here4u.view.recap.TrendsFragment
import com.example.here4u.view.emergency.Emergency
import com.example.here4u.view.profile.ProfileActivity
import com.example.here4u.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.view.LayoutInflater
import com.example.here4u.utils.FileUtils.openPdf
import com.example.here4u.utils.NetworkUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.io.File
import  com.example.here4u.view.achivements.achivements


@AndroidEntryPoint
class home : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding

    // 🟦 Cached value from LiveData (prevents multiple observers)
    private var lastPdfCached: java.io.File? = null


    companion object {
        // 🟦 Avoid repeated expensive allocations
        private val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🟦 Observe lastPdf only once (fixing microoptimization issue)
        viewModel.lastPdf.observe(this) { pdf ->
            lastPdfCached = pdf
        }

        // Navigation buttons
        binding.btnExercises.setOnClickListener {
            startActivity(Intent(this, ExercisesActivity::class.java))
        }

        binding.btnRegisterMood.setOnClickListener {
            startActivity(Intent(this, IdentifyingEmotions::class.java))
        }

        // 🟦 Fixed Recap logic without re-observing LiveData
        binding.btnRecap.setOnClickListener {
            val online = NetworkUtils.isNetworkAvailable(this)

            if (!online) {
                Log.d("Home", "Offline — checking for cached PDF")
                viewModel.getDocument() // triggers LiveData refresh

                lastPdfCached?.let { pdf ->
                    AlertDialog.Builder(this)
                        .setTitle("No Internet Connection")
                        .setMessage("Would you like to see your last saved weekly recap?")
                        .setPositiveButton("Yes") { _, _ -> openPdf(this, pdf) }
                        .setNegativeButton("No", null)
                        .show()
                } ?: Toast.makeText(
                    this,
                    "There are no recaps saved locally.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.fragmentContainer.visibility = View.VISIBLE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, TrendsFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        // 🟦 Removed duplicated listener
        binding.btnEmergency.setOnClickListener {
            startActivity(Intent(this, Emergency::class.java))
        }

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

        // Journals inflater
        binding.btnAchievements.setOnClickListener {
            startActivity(Intent(this, achivements::class.java))

        }

        binding.btnAchievements.setOnClickListener {
            startActivity(Intent(this, achivements::class.java))

        }

        val container = binding.containerJournals
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
        viewModel.refreshUserStreak()
        viewModel.refreshMoodText()
        viewModel.updatelastfive()
    }
}
