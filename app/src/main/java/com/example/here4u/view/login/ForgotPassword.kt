package com.example.here4u.view.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.here4u.R
import com.example.here4u.viewmodel.ForgotPasswordResult
import com.example.here4u.viewmodel.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

@AndroidEntryPoint
class ForgotPassword : AppCompatActivity() {

    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val emailField = findViewById<EditText>(R.id.etRecoverEmail)
        val sendButton = findViewById<Button>(R.id.btnRecover)
        val backButton = findViewById<Button>(R.id.btnBack)

        forgotPasswordViewModel.forgotPasswordResult.observe(this, Observer { result ->
            when (result) {
                is ForgotPasswordResult.Loading -> {
                    Toast.makeText(this, "Sending reset email...", Toast.LENGTH_SHORT).show()
                }
                is ForgotPasswordResult.Success -> {
                    Toast.makeText(this, "Check your inbox", Toast.LENGTH_SHORT).show()
                }
                is ForgotPasswordResult.Error -> {
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        sendButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            forgotPasswordViewModel.sendPasswordReset(email)
        }
        backButton.setOnClickListener {
            finish()
        }
        checkInternetConnection(sendButton)
    }

    private fun checkInternetConnection(sendButton: Button) {
        lifecycleScope.launch(Dispatchers.Main) {
            var wasConnected: Boolean? = null

            while (true) {
                val isConnected = withContext(Dispatchers.IO) { isConnectedToInternet() }


                if (isConnected != wasConnected) {
                    sendButton.isEnabled = isConnected

                    sendButton.alpha = if (isConnected) 1f else 0.5f

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
