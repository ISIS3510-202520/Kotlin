package com.example.here4u.data.remote.repositories

import com.example.here4u.data.remote.entity.JournalRemote
import com.example.here4u.data.remote.service.FirebaseService
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose


class JournalRemoteRepository(
    private val service: FirebaseService
) {
    fun getAll(): Flow<List<JournalRemote>> = callbackFlow {
        val listener = service.journals.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject<JournalRemote>()?.copy(id = doc.id)
                }
                trySend(list)
            }
        }
        awaitClose { listener.remove() }
    }

    fun getByEmotion(emotionId: String): Flow<List<JournalRemote>> = callbackFlow {
        val listener = service.journals
            .whereEqualTo("emotionId", emotionId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject<JournalRemote>()?.copy(id = doc.id)
                    }
                    trySend(list)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun addTextJournal(emotionId: String, userId: String, content: String): String {
        val docRef = service.journals.document()
        val journal = JournalRemote(
            id = docRef.id,
            emotionId = emotionId,
            userId = userId,
            description = content,
            createdAt = System.currentTimeMillis(),
            sharedWithTherapist = false
        )
        docRef.set(journal).await()
        return docRef.id
    }

    suspend fun deleteById(id: String) {
        service.journals.document(id).delete().await()
    }
}
