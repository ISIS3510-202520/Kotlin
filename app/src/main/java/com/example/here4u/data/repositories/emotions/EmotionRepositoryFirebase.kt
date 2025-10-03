package com.example.here4u.data.repositories.emotions

import com.example.here4u.data.remote.dto.EmotionDto
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class EmotionRepositoryFirebase {
    private val db = Firebase.firestore
    private val emotionsRef = db.collection("emotions")

    fun getEmotions(): Flow<List<EmotionDto>> = callbackFlow {
        val registration = emotionsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val emotions = snapshot?.documents.orEmpty().mapNotNull { doc ->
                doc.toObject(EmotionDto::class.java)?.copy(id = doc.id)
            }
            trySend(emotions)
        }
        awaitClose { registration.remove() }
    }


    suspend fun getEmotion(id: String): EmotionDto? {
        val document = emotionsRef.document(id).get().await()
        return if (document.exists()) {
            document.toObject(EmotionDto::class.java)?.copy(id = document.id)
        } else null
    }

    suspend fun addEmotion(emotion: EmotionDto): String {
        val id = if (emotion.id.isBlank()) emotionsRef.document().id else emotion.id
        emotionsRef.document(id).set(emotion.copy(id = id)).await()
        return id
    }


    suspend fun updateEmotion(emotion: EmotionDto) {
        require(emotion.id.isNotBlank()) { "Emotion.id no puede estar vacío para update." }
        emotionsRef.document(emotion.id).set(emotion).await()
    }


    suspend fun deleteEmotion(id: String) {
        require(id.isNotBlank()) { "Emotion.id no puede estar vacío para delete." }
        emotionsRef.document(id).delete().await()
    }
}