package com.example.here4u.view.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import javax.inject.Inject

class LoginModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        user.reload().addOnCompleteListener { reloadTask ->
                            if (reloadTask.isSuccessful) {
                                if (user.isEmailVerified) {
                                    onResult(true, null)
                                } else {

                                    onResult(false, "Your email is not verified yet. Please check your inbox or spam folder.")
                                }
                            } else {
                                onResult(false, "Error refreshing user data")
                            }
                        }
                    } else {
                        onResult(false, "User not found")
                    }
                } else {
                    val exception = task.exception
                    val errorMessage = when (exception) {
                        is FirebaseAuthInvalidUserException -> "User does not exist"
                        is FirebaseAuthInvalidCredentialsException -> "Wrong password"
                        else -> exception?.localizedMessage ?: "Unknown error"
                    }
                    onResult(false, errorMessage)
                }
            }
    }
}
