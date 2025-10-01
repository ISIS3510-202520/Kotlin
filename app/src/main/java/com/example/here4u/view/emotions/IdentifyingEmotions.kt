package com.example.here4u.view.emotions

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.GridLayoutManager
import com.example.here4u.databinding.ActivityIdentifyingEmotionsBinding
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.view.journaling.Journaling
import com.example.here4u.viewmodel.EmotionsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IdentifyingEmotions : AppCompatActivity() {

    private val viewModel: EmotionsViewModel by viewModels()
    private lateinit var binding: ActivityIdentifyingEmotionsBinding
    private lateinit var adapter: EmotionsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentifyingEmotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = EmotionsAdapter { emotion->
            val intent = Intent(this@IdentifyingEmotions, Journaling::class.java).apply {
                putExtra("emotion_name", emotion.name)
                putExtra("emotion_color", emotion.color)
                putExtra("emotion_description", emotion.description)
                putExtra("emotion_id",emotion.id)
            }
            startActivity(intent)
        }

        binding.rvEmotions.layoutManager = GridLayoutManager(this, 2)
        binding.rvEmotions.adapter = adapter

        viewModel.emotions.observe(this){
            list-> adapter.updateData(list)
        }




    }
}