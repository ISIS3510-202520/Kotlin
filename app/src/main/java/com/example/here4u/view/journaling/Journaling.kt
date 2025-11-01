package com.example.here4u.view.journaling

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.example.here4u.R
import com.example.here4u.databinding.ActivityJournalingBinding
import com.example.here4u.viewmodel.JournalingViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.here4u.view.home.home
import com.google.firebase.analytics.FirebaseAnalytics

@AndroidEntryPoint
class Journaling : AppCompatActivity() {

    private lateinit var binding: ActivityJournalingBinding
    private val viewModel: JournalingViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var emotionId: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        val smile = "#FFDBD2".toColorInt()
        val calm = "#8CC0CF".toColorInt()
        val joy = "#86D9F0".toColorInt()


        emotionId = intent.getStringExtra("emotion_id") ?: ""
        val name = intent.getStringExtra("emotion_name") ?: ""
        val colorString = intent.getStringExtra("emotion_color")
        val color = colorString?.toColorInt() ?: smile


        val iconRes = when (color) {
            smile, calm, joy -> R.drawable.sonrisa
            else -> R.drawable.llorar
        }


        binding.ivEmoji.setImageResource(iconRes)
        binding.ivEmoji.imageTintList = ColorStateList.valueOf(color)
        binding.tvEmotion.text = buildSpannedString {
            append("Describe what makes you feel\n")
            color(color) { bold { append(name) } }
        }


        binding.btnAdd.setOnClickListener {
            val text = binding.etNote.text?.toString() ?: ""

            if (text.isBlank()) {
                binding.textViewerror.visibility = View.VISIBLE
                return@setOnClickListener
            }

            binding.textViewerror.visibility = View.GONE
            viewModel.saveText(emotionId, text)
            val bundle = Bundle().apply {
                putString("emotion_id", emotionId)
                putString("emotion_name", name)
                putInt("text_length", text.length)
            }
            firebaseAnalytics.logEvent("journal_created", bundle)
            Log.d("FirebaseEvent", "Evento 'journal_created' enviado con datos: $bundle")

            val i = Intent(this@Journaling, home::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(i)
            finish()
        }
    }
}
