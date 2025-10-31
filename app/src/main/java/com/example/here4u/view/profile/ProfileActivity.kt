package com.example.here4u.view.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.here4u.R
import com.example.here4u.view.login.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import com.example.here4u.viewmodel.ProfileViewModel

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val logoutButton = findViewById<Button>(R.id.Logout)
        logoutButton.setOnClickListener {
            viewModel.logout()
        }


        viewModel.logoutResult.observe(this) { success ->
            if (success) {

                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Closed correctly", Toast.LENGTH_SHORT).show()
                finish()

            } else {

                Toast.makeText(this, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }


}


