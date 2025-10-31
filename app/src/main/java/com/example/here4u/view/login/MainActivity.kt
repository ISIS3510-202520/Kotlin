package com.example.here4u.view.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.here4u.R
import com.example.here4u.view.home.home
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.ImageView


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val emailEditText = findViewById<EditText>(R.id.etUser)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.SignUp)
        val forgotPasswordText = findViewById<TextView>(R.id.tvForgotPassword)


        checkInternetConnection(loginButton, signUpButton, forgotPasswordText)


        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginResult.Success -> {
                    Toast.makeText(this, "Successful Login!", Toast.LENGTH_SHORT).show()
                    val bundle = Bundle().apply {
                        putString(FirebaseAnalytics.Param.METHOD, "Login")
                        putString("User_email", emailEditText.text.toString())
                    }
                    firebaseAnalytics.logEvent("Login", bundle)
                    val intent = Intent(this, home::class.java)
                    startActivity(intent)
                    finish()
                }

                is LoginResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                LoginResult.Idle -> Unit
            }
        }


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (!isConnectedToInternet()) {
                showOfflineToast()

                return@setOnClickListener
            }

            loginViewModel.loginUser(email, password)
        }

        signUpButton.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

        forgotPasswordText.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
    }


    private fun isConnectedToInternet(): Boolean {
        return try {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } catch (e: Exception) {
            false
        }
    }


    private fun checkInternetConnection(
        loginButton: Button,
        signUpButton: Button,
        forgotPasswordText: TextView
    ) {
        lifecycleScope.launch(Dispatchers.Main) {
            var wasConnected: Boolean? = null

            while (true) {
                val isConnected = withContext(Dispatchers.IO) { isConnectedToInternet() }


                if (isConnected) {
                    loginButton.isEnabled = true
                    signUpButton.isEnabled = true
                    forgotPasswordText.isEnabled = true

                    if (wasConnected == false) {
                        Toast.makeText(
                            this@MainActivity,
                            "Connection restored! You can now log in.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    loginButton.isEnabled = false
                    signUpButton.isEnabled = false
                    forgotPasswordText.isEnabled = false

                    if (wasConnected != false) {
                        showOfflineToast()
                    }
                }

                wasConnected = isConnected
                delay(8000)
            }
        }
    }


    @SuppressLint("InflateParams")
    private fun showOfflineToast() {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast_photo, null)

        val textView = layout.findViewById<TextView>(R.id.tvMessage)
        val imageView = layout.findViewById<ImageView>(R.id.ivIcon)

        textView.text = "You are offline.\nTry again when you're connected."
        imageView.setImageResource(R.drawable.sad)

        val toast = Toast(this@MainActivity)
        toast.view = layout
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }
}
