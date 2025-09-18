package com.example.here4u

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExercisesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercises, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewExercises)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sampleExercises = listOf(
            Exercise("Breathing Exercise", "Calm your mind with deep breathing.", R.drawable.ic_breathing),
            Exercise("Gratitude Journal", "Write down 3 things you’re grateful for today.", R.drawable.ic_journal),
            Exercise("Mindful Walk", "Take a 10-minute mindful walk outside.", R.drawable.ic_walk)
        )

        recyclerView.adapter = ExerciseAdapter(sampleExercises)

        return view
    }
}
