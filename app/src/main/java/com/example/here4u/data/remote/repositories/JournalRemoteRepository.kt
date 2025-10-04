package com.example.here4u.data.remote.repositories

import com.example.here4u.data.remote.entity.JournalRemote
import com.example.here4u.data.remote.entity.JournalRemoteDocuments
import com.example.here4u.data.remote.service.FirebaseService
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

@Singleton
class JournalRemoteRepository @Inject constructor(
    private val service: FirebaseService,
    private val auth: FirebaseAuth
) {
    val uid = auth.currentUser?.uid

    // 🔹 Helper: map Firestore doc → JournalRemote
    private fun DocumentSnapshot.toJournal(): JournalRemote? {
        val id = id
        val emotionId = getString("emotionId") ?: return null
        val userId = getString("userId") ?: return null
        val description = getString("description") ?: ""
        val createdAt = getTimestamp("createdAt")?.toDate()?.time ?: 0L
        val shared = getBoolean("sharedWithTherapist") ?: false

        return JournalRemote(
            id = id,
            emotionId = emotionId,
            userId = userId,
            description = description,
            createdAt = createdAt,
            sharedWithTherapist = shared
        )
    }

    // 🔹 Get all journals (live updates)
    fun getAll(): Flow<List<JournalRemote>> = callbackFlow {
        val listener = service.journals.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { it.toJournal() }
                trySend(list)
            }
        }
        awaitClose { listener.remove() }
    }

    // 🔹 Get journals for a specific emotion
    fun getByEmotion(emotionId: String): Flow<List<JournalRemote>> = callbackFlow {
        val listener = service.journals
            .whereEqualTo("emotionId", emotionId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toJournal() }
                    trySend(list)
                }
            }
        awaitClose { listener.remove() }
    }

    // 🔹 Insert a new journal

    suspend fun insertOne(emotionId: String, content: String): Timestamp? =

        suspendCancellableCoroutine { cont ->
            val userId = uid
            val journal = JournalRemoteDocuments(
                emotionId = emotionId,
                userId = userId,
                description = content,
                createdAt = null,
                sharedWithTherapist = false
            )

            val ref = service.journals.add(journal)
                .addOnSuccessListener { cont.resume(journal.createdAt)
                    }
                .addOnFailureListener { throw RuntimeException("Failed submx|mit") }//cont.resumeWithException(e) }
        }

    // 🔹 Delete journal by ID
    suspend fun deleteById(id: String) =
        suspendCancellableCoroutine { cont ->
            service.journals.document(id)
                .delete()
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }



    suspend fun getLast7Days(userId: String): List<JournalRemote> =
        suspendCancellableCoroutine { cont ->
            service.journals
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val list = snapshot.documents.mapNotNull { it.toJournal() }
                    cont.resume(list)
                }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }


    // 🔹 Get last journal date for a user
    suspend fun getLastJournalDate(userId: String): Long? =
        suspendCancellableCoroutine { cont ->
            service.journals
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { snapshot ->
                    val date = snapshot.documents.firstOrNull()?.getTimestamp("createdAt")?.toDate()?.time
                    cont.resume(date)
                }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }

    // 🔹 Get most common emotion in last 7 days
    suspend fun getMostCommonEmotionLast7Days(userId: String): String? {
        val journals = getLast7Days(userId)
        if (journals.isEmpty()) return null

        return journals
            .groupingBy { it.emotionId }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
    }
}
