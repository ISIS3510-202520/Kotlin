package com.example.here4u.view.emergency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.here4u.data.remote.entity.EmergencyContactRemote
import com.example.here4u.databinding.ItemEmotionBinding
import android.content.res.Resources

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ViewHolder>()  {

    private val contacts = mutableListOf<EmergencyContactRemote>()

    fun updateData(newItems: List<EmergencyContactRemote>) {
        contacts.clear()
        contacts.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEmotionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
        holder.itemView.setOnClickListener { }
    }

    override fun getItemCount(): Int = contacts.size

    class ViewHolder(private val binding: ItemEmotionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: EmergencyContactRemote) {
            binding.btnEmotion.text = contact.name


            val screenWidth = Resources.getSystem().displayMetrics.widthPixels


            val size = (screenWidth * 0.4).toInt()


            val params = binding.btnEmotion.layoutParams
            params.width = size
            params.height = size
            binding.btnEmotion.layoutParams = params
        }
    }
}
