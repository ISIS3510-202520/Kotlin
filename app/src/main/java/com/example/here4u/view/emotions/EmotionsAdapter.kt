package com.example.here4u.view.emotions

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.here4u.databinding.ItemEmotionBinding
import com.example.here4u.data.local.entity.EmotionEntity

class EmotionsAdapter ( private val onEmotionSelected: (EmotionEntity) -> Unit):
    RecyclerView.Adapter<EmotionsAdapter.ViewHolder>() {

    private var emotionEntities: List<EmotionEntity> = emptyList()



    fun updateData(newItems: List<EmotionEntity>) {
        emotionEntities = newItems
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmotionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)

        return ViewHolder(binding)

    }
    class ViewHolder(val binding: ItemEmotionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(emotionEntity: EmotionEntity){
            binding.btnEmotion.text = emotionEntity.name
            binding.btnEmotion.backgroundTintList= ColorStateList.valueOf(emotionEntity.color)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element =emotionEntities[position]
        holder.bind(element)
        holder.binding.btnEmotion.setOnClickListener { onEmotionSelected(element) }

    }


    override fun getItemCount(): Int {
        return emotionEntities.size

    }




}