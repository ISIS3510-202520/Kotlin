package com.example.here4u.model

import android.util.Patterns
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class SignupModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    // --- Validaciones ---
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,}\$")
        return passwordRegex.matches(password)
    }

    // --- Registro de usuario ---
    fun signup(
        email: String,
        name: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (!isEmailValid(email)) {
            onResult(false, "Not a valid email")
            return
        }

        if (!isPasswordValid(password)) {
            onResult(
                false,
                "Password must have at least 8 characters, one uppercase letter and one special character"
            )
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser

                    if (user != null) {
                        // Enviar correo de verificación
                        user.sendEmailVerification()
                            .addOnCompleteListener { verifyTask ->
                                if (verifyTask.isSuccessful) {
                                    // Crear documento en Firestore
                                    createUserInFirestore(user.uid, name, email) { success, error ->
                                        if (success) {
                                            onResult(true, "Verification email sent. User saved in Firestore.")
                                        } else {
                                            onResult(false, error ?: "Error saving user in Firestore")
                                        }
                                    }
                                } else {
                                    val error = verifyTask.exception?.localizedMessage
                                        ?: "Error sending verification email"
                                    onResult(false, error)
                                }
                            }
                    } else {
                        onResult(false, "User is null after registration")
                    }

                } else {
                    val error = task.exception?.localizedMessage ?: "Unknown error during registration"
                    onResult(false, error)
                }
            }
    }

    // --- Crear documento de usuario en Firestore ---
    private fun createUserInFirestore(
        uid: String,
        name: String,
        email: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val db = Firebase.firestore
        val user = User(
            id = uid,
            displayName = name,
            email = email,
            createdAt = Timestamp.now(),
            lastLogin = Timestamp.now(),
            lastEntryDate = Timestamp.now(),
            currentStreak = 0,
            longestStreak = 0
        )

        db.collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.localizedMessage)
            }
    }

    // --- Data class de usuario ---
    data class User(
        val id: String = "",
        val displayName: String = "",
        val email: String = "",
        val createdAt: Timestamp? = null,
        val lastLogin: Timestamp? = null,
        val lastEntryDate: Timestamp? = null,
        val currentStreak: Int = 0,
        val longestStreak: Int = 0
    )
}
