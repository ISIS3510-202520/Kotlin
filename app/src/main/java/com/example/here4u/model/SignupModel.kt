package com.example.here4u.model

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignupModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}\$")
        return passwordRegex.matches(password)
    }

    fun signup(email: String, name: String, password: String, onResult: (Boolean, String?) -> Unit) {

        if (!isEmailValid(email)) {
            onResult(false, "not valid email")
            return
        }

        if (!isPasswordValid(password)) {
            onResult(
                false,
                "password must have 8 characters, at least one uppercase letter and one special character"
            )
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verifyTask ->
                            if (verifyTask.isSuccessful) {
                                onResult(true, "Verification email sent. Please check your inbox.")
                            } else {
                                val error = verifyTask.exception?.localizedMessage
                                    ?: "Error sending verification email"
                                onResult(false, error)
                            }
                        }
                } else {
                    val error = task.exception?.localizedMessage ?: "unknown error at register"
                    onResult(false, error)
                }
            }
    }
}
