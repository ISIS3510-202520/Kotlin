package com.example.here4u.data.remote.repositories

import com.example.here4u.data.remote.entity.EmergencyContactEntity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EmergencyContactRemoteRepository @Inject constructor(
    private val userRepository: UserRemoteRepository
) {

    private val db = Firebase.firestore

    suspend fun addEmergencyContact(contact: EmergencyContactEntity): Boolean {
        return try {
            val userId = userRepository.getUserId() ?: return false

            val docRef = db.collection("emergency_contacts").document()
            val contactWithId = contact.copy(
                id = docRef.id,
                userID = userId
            )

            docRef.set(contactWithId).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getContactsForCurrentUser(): List<EmergencyContactEntity> {
        return try {
            val userId = userRepository.getUserId() ?: return emptyList()

            val snapshot = db.collection("emergency_contacts")
                .whereEqualTo("userID", userId)
                .get()
                .await()

            snapshot.toObjects(EmergencyContactEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteContactsForCurrentUser() {
        val userId = userRepository.getUserId() ?: return
        val contacts = getContactsForCurrentUser()

        contacts.forEach {
            db.collection("emergency_contacts").document(it.id).delete()
        }
    }
}