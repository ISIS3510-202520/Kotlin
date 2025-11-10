package com.example.here4u.data.remote.repositories

import com.example.here4u.data.local.entity.JournalEntity
import com.example.here4u.data.local.repositories.JournalLocalRepository
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
import java.util.Date

@Singleton
class JournalRemoteRepository @Inject constructor(
    private val service: FirebaseService,
    private val auth: FirebaseAuth,

) {
    val uid = auth.currentUser?.uid


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



    suspend fun insertOne(emotionId: String, content: String,time: Timestamp=Timestamp(Date(0)), ui: String?=null): Timestamp? =
        suspendCancellableCoroutine { cont ->
            var userId = uid
            if (ui != null){
                userId=ui
            }
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
                .addOnFailureListener { throw RuntimeException("Failed submx|mit") }
        }



    suspend fun deleteById(id: String) =
        suspendCancellableCoroutine { cont ->
            service.journals.document(id)
                .delete()
                .addOnSuccessListener { cont.resume(Unit) }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }

    suspend fun getLast5Journals(userId: String): List<JournalEntity> {
        val snapshot = service.journals
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val remote = doc.toObject(JournalRemoteDocuments::class.java)
            remote?.let {
                JournalEntity(
                    userId = it.userId ?: "",
                    emotionId = it.emotionId ?: "",
                    description = it.description,
                    createdAt = it.createdAt ?: com.google.firebase.Timestamp.now(),
                    sync = true
                )
            }
        }
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






}
