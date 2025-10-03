package com.example.here4u.data.remote.repositories

import com.example.here4u.data.remote.entity.UserRemote
import com.example.here4u.data.remote.service.FirebaseService
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose


class UserRemoteRepository(
    private val service: FirebaseService
) {
    suspend fun createUser(user: UserRemote) {
        service.users.document(user.id).set(user).await()
    }

    suspend fun getUserById(id: String): UserRemote? {
        val doc = service.users.document(id).get().await()
        return doc.toObject<UserRemote>()?.copy(id = doc.id)
    }

    fun getAll(): Flow<List<UserRemote>> = callbackFlow {
        val listener = service.users.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject<UserRemote>()?.copy(id = doc.id)
                }
                trySend(list)
            }
        }
        awaitClose { listener.remove() }
    }
}
