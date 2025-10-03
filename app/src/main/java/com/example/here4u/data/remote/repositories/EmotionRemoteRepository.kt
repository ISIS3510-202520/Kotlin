package com.example.here4u.data.remote.repositories

import com.example.here4u.data.remote.entity.EmotionRemote
import com.example.here4u.data.remote.service.FirebaseService
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose


class EmotionRemoteRepository(
    private val service: FirebaseService
) {
    fun getAll(): Flow<List<EmotionRemote>> = callbackFlow {
        val listener = service.emotions.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject<EmotionRemote>()?.copy(id = doc.id)
                }
                trySend(list)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun insertOne(item: EmotionRemote) {
        service.emotions.document(item.id).set(item).await()
    }

    suspend fun deleteById(id: String) {
        service.emotions.document(id).delete().await()
    }

    suspend fun getById(id: String): EmotionRemote? {
        val doc = service.emotions.document(id).get().await()
        return doc.toObject<EmotionRemote>()?.copy(id = doc.id)
    }
}
