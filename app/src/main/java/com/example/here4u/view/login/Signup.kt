package com.example.here4u.view.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.here4u.R
import com.example.here4u.viewmodel.SignupResult
import com.example.here4u.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import android.widget.Toast


@AndroidEntryPoint
class Signup : AppCompatActivity() {

    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val nameField = findViewById<EditText>(R.id.etName)
        val emailField = findViewById<EditText>(R.id.etEmail)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val signupButton = findViewById<Button>(R.id.btnSignup)
        val backButton = findViewById<Button>(R.id.btnBack)

        signupViewModel.signupResult.observe(this, Observer { result ->
            when (result) {
                is SignupResult.Success -> {
                    finish()
                }
                is SignupResult.Error -> {

                }
                else -> {}
            }
        })

        signupButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            signupViewModel.signup(email, name, password)
        }

        backButton.setOnClickListener {
            finish()
        }

        checkInternetConnection(signupButton)
    }

    private fun checkInternetConnection(signupButton: Button) {
        lifecycleScope.launch(Dispatchers.Main) {
            var wasConnected: Boolean? = null


            while (true) {
                val isConnected = withContext(Dispatchers.IO) { isConnectedToInternet() }


                if (isConnected != wasConnected) {
                    signupButton.isEnabled = isConnected




                    wasConnected = isConnected
                }

                delay(3000)
            }
        }
    }

    private fun isConnectedToInternet(): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }

}
