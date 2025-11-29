package com.example.here4u.view.exercises

import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.here4u.databinding.ActivityBreathingExerciseBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BreathingExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreathingExerciseBinding
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreathingExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartBreathing.setOnClickListener {
            if (!isRunning) startBreathingExercise()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun startBreathingExercise() {
        isRunning = true

        lifecycleScope.launch(Dispatchers.Main) {

            repeat(3) { cycle ->
                binding.tvStatus.text = "Inhale..."
                animateCircle(expand = true)
                delay(4000)

                binding.tvStatus.text = "Hold..."
                delay(2000)

                binding.tvStatus.text = "Exhale..."
                animateCircle(expand = false)
                delay(4000)
            }

            binding.tvStatus.text = "Completed"
            Toast.makeText(this@BreathingExerciseActivity, "Great job! 💙", Toast.LENGTH_SHORT).show()

            // Optional: guardar progreso
            withContext(Dispatchers.IO) {
                saveBreathingSession()
            }

            isRunning = false
        }
    }

    private fun animateCircle(expand: Boolean) {
        val startSize = if (expand) 200f else 500f
        val endSize   = if (expand) 500f else 200f

        ValueAnimator.ofFloat(startSize, endSize).apply {
            duration = 4000
            addUpdateListener { animator ->
                val value = animator.animatedValue as Float
                binding.breathCircle.layoutParams.width = value.toInt()
                binding.breathCircle.layoutParams.height = value.toInt()
                binding.breathCircle.requestLayout()
            }
            start()
        }
    }

    private fun saveBreathingSession() {

        Thread.sleep(400)
    }
}
