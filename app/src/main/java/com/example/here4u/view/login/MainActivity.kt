package com.example.here4u.view.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.here4u.R
import com.example.here4u.view.home.home
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.Lifecycle


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent("app_connection_test", Bundle().apply {
            putString("event_origin", "MainActivity")
            putString("status", "app_opened")
        })

        val emailEditText = findViewById<EditText>(R.id.etUser)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.SignUp)
        val forgotPasswordText = findViewById<TextView>(R.id.tvForgotPassword)

        // ---------------------------
        // 🔥 MICRO OPTIMIZATION
        // Replaces infinite loop with lifecycle-aware collector
        // ---------------------------
        collectInternetStatus(loginButton, signUpButton, forgotPasswordText)

        // ---------------------------
        // LiveData observer (safe, defined once)
        // ---------------------------
        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginResult.Success -> {
                    Toast.makeText(this, "Successful Login!", Toast.LENGTH_SHORT).show()

                    firebaseAnalytics.logEvent("login_event", Bundle().apply {
                        putString(FirebaseAnalytics.Param.METHOD, "Login")
                        putString("User_email", emailEditText.text.toString())
                    })

                    startActivity(Intent(this, home::class.java))
                    finish()
                }

                is LoginResult.Error ->
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()

                LoginResult.Idle -> Unit
            }
        }

        // ---------------------------
        // Click listeners (no observers added here)
        // ---------------------------
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

    // ---------------------------------------------------
    // 🔥 Micro-optimized, lifecycle-aware internet monitor
    // ---------------------------------------------------
    private fun collectInternetStatus(
        loginButton: Button,
        signUpButton: Button,
        forgotPasswordText: TextView
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                var wasConnected: Boolean? = null

                while (true) {
                    val isConnected = withContext(Dispatchers.IO) { isConnectedToInternet() }

                    loginButton.isEnabled = isConnected
                    signUpButton.isEnabled = isConnected
                    forgotPasswordText.isEnabled = isConnected

                    if (isConnected && wasConnected == false) {
                        showOnlineToast()
                    } else if (!isConnected && wasConnected != false) {
                        showOfflineToast()
                    }

                    wasConnected = isConnected
                    delay(3000)
                }
            }
        }
    }

    private fun isConnectedToInternet(): Boolean {
        return try {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetwork
            val caps = cm.getNetworkCapabilities(network)
            caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } catch (_: Exception) {
            false
        }
    }

    // ---------------------------
    // Custom Toasts
    // ---------------------------
    @SuppressLint("InflateParams")
    fun showOfflineToast() {
        val layout = layoutInflater.inflate(R.layout.custom_toast_photo, null)
        layout.findViewById<TextView>(R.id.tvMessage).text = "You are offline."
        layout.findViewById<ImageView>(R.id.ivIcon).setImageResource(R.drawable.sad)

        Toast(this).apply {
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_LONG
        }.show()
    }

    @SuppressLint("InflateParams")
    fun showOnlineToast() {
        val layout = layoutInflater.inflate(R.layout.custom_toast_photo, null)
        layout.findViewById<TextView>(R.id.tvMessage).text = "Connection restored!"
        layout.findViewById<ImageView>(R.id.ivIcon).setImageResource(R.drawable.happy)

        Toast(this).apply {
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_LONG
        }.show()
    }
}
