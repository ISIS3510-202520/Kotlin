package com.example.here4u.data.repositories.journal

import android.R
import com.google.firebase.firestore.DocumentReference
import kotlin.String
import com.example.here4u.data.remote.dto.JournalDto
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class JournalRepositoryFirebase {
    private val db = Firebase.firestore


    private fun journalsRef(userId: String) =
        db.collection("users").document(userId).collection("journals")


    fun getByDateRange(from:Long,userId: String): Flow<List<JournalDto>> = callbackFlow {
            val query=journalsRef(userId).whereGreaterThanOrEqualTo("createdAt",from)


            val registration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
                val journals = snapshot?.documents.orEmpty().mapNotNull { doc ->
                    doc.toObject(JournalDto::class.java)?.copy(id = doc.id)
                }
                trySend(journals)}

        awaitClose { registration.remove() }
    }

    fun getByEmotion(emotionId: String,userId:String): Flow<List<JournalDto>> = callbackFlow {
        val query=journalsRef(userId).whereEqualTo("id",emotionId)

        val registration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val journals = snapshot?.documents.orEmpty().mapNotNull { doc ->
                doc.toObject(JournalDto::class.java)?.copy(id = doc.id)
            }
            trySend(journals)}

        awaitClose { registration.remove() }
    }


    suspend fun addJournal(id:String, emotionId:String, userId:String, description: String): String {
        val journal = JournalDto( id,
        userId,
        emotionId,
        description, System.currentTimeMillis(),
        false)

        val id = id.ifBlank { journalsRef(userId).document().id }
        journalsRef(userId).document(id).set(journal).await()
        return id
    }


    suspend fun deleteJournal(id: String,userId: String) {
        require(id.isNotBlank()) { "Emotion.id no puede estar vacío para delete." }
        journalsRef(userId).document(id).delete().await()
    }

    suspend fun fetchLastRemoteCreatedAt(userId: String): Long {
        val snap = journalsRef (userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        val last = snap.documents.firstOrNull()
        return last?.getLong("createdAt") ?: 0L
    }
    suspend fun upsertAll(dtos: List<JournalDto>) {
        if (dtos.isEmpty()) return
        val chunks = dtos.chunked(450)
        for (chunk in chunks) {
            val batch = db.batch()
            chunk.forEach { dto ->
                val ref = journalsRef(dto.userId).document(dto.id.ifBlank {
                    journalsRef(dto.userId).document().id
                })
                batch.set(ref, dto.copy(id = ref.id))
            }
            batch.commit().await()
        }
    }

}