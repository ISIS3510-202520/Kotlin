package com.example.here4u

import android.R.id.bold
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.example.here4u.databinding.ActivityIdentifyingEmotionsBinding
import com.example.here4u.databinding.ActivityJournalingBinding



class Journaling : AppCompatActivity() {

    private lateinit var binding: ActivityJournalingBinding

    @SuppressLint("SetTextI18n")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("emotion_name")
        val color = intent.getIntExtra("emotion_color", android.graphics.Color.BLACK)
        val smile = "#FFDBD2".toColorInt()
        val calm  = "#8CC0CF".toColorInt()
        val joy   = "#86D9F0".toColorInt()

        val iconRes = when (color) {
            smile, calm, joy -> R.drawable.sonrisa
            else             -> R.drawable.llorar
        }

        binding.ivEmoji.setImageResource(iconRes)
        binding.ivEmoji.imageTintList = ColorStateList.valueOf(color)
        binding.tvEmotion.text = buildSpannedString {
            append("Describe what makes you feel\n")
            color(color) {
                bold { append(name) }
            }
        }

    }}



