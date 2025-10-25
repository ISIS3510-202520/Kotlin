package com.example.here4u.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.here4u.R
import com.example.here4u.view.home.home
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.analytics.FirebaseAnalytics

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
        val SignUp = findViewById<Button>(R.id.SignUp)
        val forgotPasswordText = findViewById<TextView>(R.id.tvForgotPassword)




        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginResult.Success -> {
                    Toast.makeText(this, "Sucessfull Login !", Toast.LENGTH_SHORT).show()
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
                LoginResult.Idle -> {

                }
            }
        }
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginViewModel.loginUser(email, password)



            }

        SignUp.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }


        forgotPasswordText.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }


    }

}
