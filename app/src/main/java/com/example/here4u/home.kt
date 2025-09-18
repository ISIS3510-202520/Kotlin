package com.example.here4u

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val trendsButton = findViewById<Button>(R.id.btnRecap)
        val fragmentContainer = findViewById<androidx.fragment.app.FragmentContainerView>(R.id.fragmentContainer)

        trendsButton.setOnClickListener {
            fragmentContainer.visibility = View.VISIBLE // show container
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TrendsFragment())
                .addToBackStack(null) // allows back navigation
                .commit()
        }
    }
}
