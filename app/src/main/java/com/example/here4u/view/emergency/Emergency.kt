package com.example.here4u.view.emergency

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.here4u.R
import com.example.here4u.databinding.ActivityEmergencyBinding

class Emergency : AppCompatActivity() {

    private lateinit var binding: ActivityEmergencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }

}