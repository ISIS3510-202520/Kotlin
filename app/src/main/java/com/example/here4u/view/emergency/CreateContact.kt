package com.example.here4u.view.emergency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.here4u.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateContact: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contact)

    }

}