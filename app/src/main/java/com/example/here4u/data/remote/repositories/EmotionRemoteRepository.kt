package com.example.here4u.data.remote.repositories

import android.content.ContentValues.TAG
import android.util.Log
import com.example.here4u.data.remote.entity.EmotionRemote
import com.example.here4u.data.remote.service.FirebaseService
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class EmotionRemoteRepository(
) {

    val db = Firebase.firestore
    fun getAll(): Flow<List<EmotionRemote>> = callbackFlow {
        val reg: ListenerRegistration =
            db.collection("emotions").addSnapshotListener { snap, err ->
                if (err != null) {
                    close(err); return@addSnapshotListener
                }
                val list = snap?.toObjects(EmotionRemote::class.java) ?: emptyList()
                trySend(list)
            }
        awaitClose { reg.remove() }
    }


    suspend fun deleteById(id: String) {
        db.collection("emotions").document(id).delete().addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

    }


    suspend fun getById(id: String): EmotionRemote? =
        suspendCancellableCoroutine { cont ->
            db.collection("emotions").document(id).get()
                .addOnSuccessListener { snap ->
                    val obj = snap.toObject(EmotionRemote::class.java)?.copy(id = snap.id)
                    cont.resume(obj)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "getById failed", e)
                    cont.resume(null)
                }
        }
}
