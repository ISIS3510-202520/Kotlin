package com.example.here4u.view.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is SignupResult.Error -> {
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
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

        monitorWifiConnection(signupButton)
    }

    private fun monitorWifiConnection(signupButton: Button) {
        lifecycleScope.launch(Dispatchers.Main) {
            var wasConnected: Boolean? = null

            while (true) {
                val isConnected = withContext(Dispatchers.IO) { isConnectedToWifi() }

                if (isConnected != wasConnected) {
                    signupButton.isEnabled = isConnected
                    if (!isConnected) {
                        Toast.makeText(
                            this@Signup,
                            "Conéctate a una red Wi-Fi para registrarte",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    wasConnected = isConnected
                }

                delay(3000)
            }
        }
    }

    private fun isConnectedToWifi(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
}
