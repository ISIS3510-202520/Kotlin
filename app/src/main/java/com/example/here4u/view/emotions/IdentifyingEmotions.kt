package com.example.here4u.view.emotions

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.GridLayoutManager
import com.example.here4u.databinding.ActivityIdentifyingEmotionsBinding
import com.example.here4u.model.Emotion
import com.example.here4u.view.journaling.Journaling

class IdentifyingEmotions : AppCompatActivity() {
    val C_PEACH = "#FFDBD2".toColorInt()
    val C_TEAL  = "#8CC0CF".toColorInt()
    val C_SKY   = "#86D9F0".toColorInt()
    val C_SLATE = "#7C8FBB".toColorInt()


    val emotions = listOf(
        // JOY (peach)
        Emotion("Serenity", C_PEACH),
        Emotion("Joy", C_PEACH),
        Emotion("Ecstasy", C_PEACH),

        // TRUST (teal)
        Emotion("Acceptance", C_TEAL),
        Emotion("Trust", C_TEAL),
        Emotion("Admiration", C_TEAL),

        // SURPRISE (sky)
        Emotion("Distraction", C_SKY),
        Emotion("Surprise", C_SKY),
        Emotion("Amazement", C_SKY),

        // SADNESS (slate)
        Emotion("Pensiveness", C_SLATE),
        Emotion("Sadness", C_SLATE),
        Emotion("Grief", C_SLATE)
    )
    private lateinit var binding: ActivityIdentifyingEmotionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIdentifyingEmotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvEmotions.adapter = EmotionsAdapter(emotions) { element ->
            val intent = Intent(this, Journaling::class.java)
            intent.putExtra("emotion_name", element.name)
            intent.putExtra("emotion_color", element.color)
            startActivity(intent)
        }
        binding.rvEmotions.layoutManager = GridLayoutManager(this, 2)


    }
}