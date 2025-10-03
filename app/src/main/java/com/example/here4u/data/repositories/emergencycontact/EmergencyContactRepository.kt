package com.example.here4u.data.repositories.emergencycontact

import com.example.here4u.data.remote.dto.EmergencyContactDto
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class EmergencyContactRepository (userId: String) {
    private val dbfirebase = Firebase.firestore
    private val collection= dbfirebase.collection("users").document(userId).collection("contacts")


    suspend fun addContact(contact: EmergencyContactDto): String {
        val id = contact.id.ifBlank { collection.document().id }
        collection.document(id).set(contact).await()
        return id
    }

    suspend fun updateContact(contact: EmergencyContactDto) {
        collection.document(contact.id).set(contact).await()
    }

    suspend fun deleteContact(id: String) {

        collection.document(id).delete().await()
    }

    fun observeAll(): Flow<List<EmergencyContactDto>> = callbackFlow {
        val reg = collection
            .addSnapshotListener { snap, err ->
                if (err != null) {
                    close(err); return@addSnapshotListener
                }
                val items = snap?.documents.orEmpty().mapNotNull { doc ->
                    doc.toObject(EmergencyContactDto::class.java)?.copy(id = doc.id)
                }
                trySend(items).isSuccess
            }
        awaitClose { reg.remove() }
    }



    }