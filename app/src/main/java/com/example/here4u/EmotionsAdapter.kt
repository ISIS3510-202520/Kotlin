package com.example.here4u

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.here4u.databinding.ItemEmotionBinding
import com.example.here4u.model.Emotion

class EmotionsAdapter (private val emotions: List<Emotion>, private val onEmotionSelected: (Emotion) -> Unit):
    RecyclerView.Adapter<EmotionsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmotionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)

        return ViewHolder(binding)

    }
    class ViewHolder(val binding:ItemEmotionBinding ) : RecyclerView.ViewHolder(binding.root){
        fun bind(emotion: Emotion){
            binding.btnEmotion.text = emotion.name
            binding.btnEmotion.backgroundTintList= ColorStateList.valueOf(emotion.color)
        }
    }

    override fun onBindViewHolder(holder: EmotionsAdapter.ViewHolder, position: Int) {
        val element =emotions[position]
        holder.bind(element)
        holder.binding.btnEmotion.setOnClickListener { onEmotionSelected(element) }

    }


    override fun getItemCount(): Int {
        return emotions.size

    }




}