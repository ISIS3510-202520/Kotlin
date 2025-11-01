package com.example.here4u.view.emotions

import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.here4u.databinding.ItemEmotionBinding
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.remote.entity.EmotionRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class EmotionsAdapter ( private val onEmotionSelected: (EmotionRemote) -> Unit):
    RecyclerView.Adapter<EmotionsAdapter.ViewHolder>() {

    private val items = mutableListOf<EmotionRemote>()



    fun updateData(newItems: List<EmotionRemote>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmotionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)

        return ViewHolder(binding)

    }
    class ViewHolder(val binding: ItemEmotionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(emotionEntity: EmotionRemote){
            binding.btnEmotion.text = emotionEntity.name
            binding.btnEmotion.backgroundTintList= ColorStateList.valueOf(emotionEntity.colorHex.toColorInt())
            val screenWidth = Resources.getSystem().displayMetrics.widthPixels


            val size = (screenWidth * 0.4).toInt()


            val params = binding.btnEmotion.layoutParams
            params.width = size
            params.height = size
            binding.btnEmotion.layoutParams = params
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element =items[position]
        holder.bind(element)
        holder.binding.btnEmotion.setOnClickListener { onEmotionSelected(element) }

    }


    override fun getItemCount(): Int {
        return items.size

    }

}