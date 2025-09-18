package com.example.here4u

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.here4u.databinding.ItemEmotionBinding

class EmotionsAdapter (private val emotions: List<Emotion>):
    RecyclerView.Adapter<EmotionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmotionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)

        return ViewHolder(binding)

    }

    class ViewHolder(private val binding:ItemEmotionBinding ) : RecyclerView.ViewHolder(binding.root){
        fun bind(emotion: Emotion){
            binding.btnEmotion.text = emotion.name
            binding.btnEmotion.backgroundTintList= ColorStateList.valueOf(emotion.color)

        }}

    override fun onBindViewHolder(holder: EmotionsAdapter.ViewHolder, position: Int) {
        holder.bind(emotions[position])
    }


    override fun getItemCount(): Int {
        return emotions.size

    }


}