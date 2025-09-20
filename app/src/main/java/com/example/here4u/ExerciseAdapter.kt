package com.example.here4u
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseAdapter(private val exercises: List<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    // ViewHolder class: holds the references to the views in each item
    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.exerciseIcon)
        val title: TextView = itemView.findViewById(R.id.exerciseTitle)
        val description: TextView = itemView.findViewById(R.id.exerciseDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.icon.setImageResource(exercise.iconResId)
        holder.title.text = exercise.title
        holder.description.text = exercise.description
    }

    override fun getItemCount(): Int = exercises.size
}
