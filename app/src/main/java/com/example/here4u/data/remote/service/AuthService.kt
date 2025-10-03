package com.example.here4u.data.remote.service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class AuthService(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    val currentUser get() = auth.currentUser

    fun isSignedIn(): Boolean = currentUser != null

    suspend fun signInAnonymously(): FirebaseUser? =
        suspendCoroutine { cont ->
            auth.signInAnonymously()
                .addOnSuccessListener { result -> cont.resume(result.user) }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }

    suspend fun signInWithEmail(email: String, password: String): FirebaseUser? =
        suspendCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result -> cont.resume(result.user) }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }

    fun signOut() = auth.signOut()
}
