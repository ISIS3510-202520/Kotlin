package com.example.here4u.view.login

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.here4u.R
import com.example.here4u.viewmodel.SignupResult
import com.example.here4u.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is SignupResult.Error -> {
                    showBigCenteredToast(" ${result.message}")
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
    }
    private fun showBigCenteredToast(message: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val textView = layout.findViewById<TextView>(R.id.tvMessage)
        textView.text = message

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }



}
