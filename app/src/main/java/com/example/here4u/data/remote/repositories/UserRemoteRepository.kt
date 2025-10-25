package com.example.here4u.data.remote.repositories

import android.util.Log.e
import com.example.here4u.data.remote.entity.UserRemote
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun registerUser(
        email: String,
        password: String,
        name: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {


                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                        }

                        user.updateProfile(profileUpdates).addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {


                                user.sendEmailVerification().addOnCompleteListener { verifyTask ->
                                    if (verifyTask.isSuccessful) {


                                        createUserDocument(user.uid, email, name) { success, error ->
                                            if (success) {
                                                onResult(true, "Verification email sent. User saved in Firestore.")
                                            } else {
                                                onResult(false, error)
                                            }
                                        }

                                    } else {
                                        val error = verifyTask.exception?.localizedMessage
                                            ?: "Error sending verification email"
                                        onResult(false, error)
                                    }
                                }

                            } else {
                                val error = updateTask.exception?.localizedMessage
                                    ?: "Error updating user profile"
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

    fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid

    }

    fun getName(): String? {
        return firebaseAuth.currentUser?.displayName
    }

    private fun createUserDocument(
        uid: String,
        email: String,
        name: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val db = Firebase.firestore
        val now = Timestamp.now()
        val userEntity = UserRemote(
            id = uid,
            displayName = name,
            email = email,
            createdAt = now,
            lastLogin = now,
            lastEntryDate = null ,
            currentStreak = 1,
            longestStreak = 1
        )

        db.collection("users").document(uid)
            .set(userEntity)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.localizedMessage) }
    }

    suspend fun updatelogindate(){
        val userId = getUserId() ?: return
        val db = Firebase.firestore
        val userRef = db.collection("users").document(userId)

        try{
            userRef.update(
                mapOf(
                    "lastLogin" to Timestamp.now()
                )
            ).await()
        }
        catch (e: Exception) {
            android.util.Log.e("STREAK", "Error actualizando last login :) ${e.message}", e)
        }
    }
    suspend fun updateLoginStreak() {
        val userId = getUserId() ?: return
        val db = Firebase.firestore
        val userRef = db.collection("users").document(userId)

        try {
            val snapshot = userRef.get().await()
            if (!snapshot.exists()) {
                android.util.Log.d("STREAK", "Documento no encontrado")
                return
            }

            val user = snapshot.toObject(com.example.here4u.data.remote.entity.UserRemote::class.java)
            if (user == null) {
                android.util.Log.d("STREAK", "No se pudo mapear el usuario")
                return
            }

            val lastEntry = user.lastEntryDate?.toDate()
            val now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time

            if (lastEntry == null) {
                android.util.Log.d("STREAK", "lastEntry es null, inicializando racha en 1")
                userRef.update(
                    mapOf(
                        "lastEntryDate" to Timestamp.now(),
                        "currentStreak" to 1,
                        "longestStreak" to 1
                    )
                ).await()
                return
            }

            // Normalizar fechas a medianoche UTC
            val calLast = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                time = lastEntry
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val calNow = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                time = now
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val diffDays = ((calNow.timeInMillis - calLast.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
            android.util.Log.d("STREAK", "diffDays=$diffDays")

            var newStreak = user.currentStreak
            var longest = user.longestStreak

            when (diffDays) {
                0 -> {
                    android.util.Log.d("STREAK", "Ya se registró entrada hoy, no se modifica racha")
                    return
                }
                1 -> {
                    newStreak += 1
                    if (newStreak > longest) longest = newStreak
                }
                else -> {
                    newStreak = 1
                }
            }

            userRef.update(
                mapOf(
                    "lastEntryDate" to Timestamp.now(), //
                    "currentStreak" to newStreak,
                    "longestStreak" to longest
                )
            ).await()

            android.util.Log.d("STREAK", "Racha actualizada correctamente: $newStreak (max: $longest)")

        } catch (e: Exception) {
            android.util.Log.e("STREAK", "Error actualizando racha: ${e.message}", e)
        }
    }

    suspend fun updateLastEntry(timestamp: Timestamp?){
        val userId = getUserId() ?: return
        val db = Firebase.firestore
        val userRef = db.collection("users").document(userId)
        try {
            val snapshot = userRef.get().await()
            if (!snapshot.exists()) {
                android.util.Log.d("Journal Update", "Documento no encontrado")
                return
            }

            val user = snapshot.toObject(com.example.here4u.data.remote.entity.UserRemote::class.java)
            if (user == null) {
                android.util.Log.d("Journal Update", "No se pudo mapear el usuario")
                return
            }

            userRef.update(
                mapOf(
                    "lastEntryDate" to Timestamp.now(),
                )
            )


        } catch (e: Exception) {
            android.util.Log.e("v", "Error actualizando last entry date: ${e.message}", e)
        }
    }



    suspend fun getUserStreak(): Pair<Int, Int>? {
        val userId = getUserId()?: return null
        val db = Firebase.firestore
        val userRef = db.collection("users").document(userId)

        return try {
            val snapshot = userRef.get().await()
            if (snapshot.exists()) {
                val current = snapshot.getLong("currentStreak")?.toInt() ?: 0
                val longest = snapshot.getLong("longestStreak")?.toInt() ?: 0
                Pair(current, longest)
            } else {
                null
            }
        } catch (e: Exception) {
            android.util.Log.e("STREAK", "Error fetching streak: ${e.message}")
            null
        }
    }


    suspend fun logout(): Boolean {
        try {
            firebaseAuth.signOut()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false

        }


    }

}
