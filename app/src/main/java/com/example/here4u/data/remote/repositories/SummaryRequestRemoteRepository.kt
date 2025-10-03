package com.example.here4u.data.remote.repositories

import com.example.here4u.data.remote.entity.SummaryRequestRemote
import com.example.here4u.data.remote.service.FirebaseService
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SummaryRequestRemoteRepository @Inject constructor(
    private val firebaseService: FirebaseService
) {

    /**
     * Inserts a new summary request into Firestore.
     * Returns the generated ID of the new document.
     */
    suspend fun insertOne(request: SummaryRequestRemote): String =
        suspendCancellableCoroutine { cont ->
            val id = if (request.id.isBlank()) UUID.randomUUID().toString() else request.id
            val newRequest = request.copy(id = id)

            firebaseService.users
                .document(newRequest.userId)
                .collection("summaryRequests") // 👈 nested under the user
                .document(id)
                .set(newRequest)
                .addOnSuccessListener { cont.resume(id) }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }
}
