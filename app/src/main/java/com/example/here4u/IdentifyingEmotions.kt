package com.example.here4u

import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.GridLayoutManager
import com.example.here4u.databinding.ActivityIdentifyingEmotionsBinding

class IdentifyingEmotions : AppCompatActivity() {
    val C_PEACH = "#FFDBD2".toColorInt()
    val C_TEAL  = "#8CC0CF".toColorInt()
    val C_SKY   = "#86D9F0".toColorInt()
    val C_SLATE = "#7C8FBB".toColorInt()


    val emotions = listOf(
        // JOY (peach)
        Emotion("Serenity",  C_PEACH),
        Emotion("Joy",       C_PEACH),
        Emotion("Ecstasy",   C_PEACH),

        // TRUST (teal)
        Emotion("Acceptance", C_TEAL),
        Emotion("Trust",      C_TEAL),
        Emotion("Admiration", C_TEAL),

        // SURPRISE (sky)
        Emotion("Distraction", C_SKY),
        Emotion("Surprise",    C_SKY),
        Emotion("Amazement",   C_SKY),

        // SADNESS (slate)
        Emotion("Pensiveness", C_SLATE),
        Emotion("Sadness",     C_SLATE),
        Emotion("Grief",       C_SLATE)
    )
    private lateinit var binding: ActivityIdentifyingEmotionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIdentifyingEmotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvEmotions.adapter = EmotionsAdapter(emotions)
        binding.rvEmotions.layoutManager = GridLayoutManager(this, 2)


    }
}