package com.example.here4u.view.journaling

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.example.here4u.R
import com.example.here4u.databinding.ActivityJournalingBinding

class Journaling : AppCompatActivity() {

    private lateinit var binding: ActivityJournalingBinding

    @SuppressLint("SetTextI18n")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("emotion_name")
        val color = intent.getIntExtra("emotion_color", Color.BLACK)
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