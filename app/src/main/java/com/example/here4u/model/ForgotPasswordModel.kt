package com.example.here4u.model

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class ForgotPasswordModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun sendPasswordResetEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank()) {
            onResult(false, "Email is required")
            return
        }

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Password reset email sent. Check your inbox.")
                } else {
                    val error = task.exception?.localizedMessage ?: "Error sending reset email"
                    onResult(false, error)
                }
            }
    }
}
