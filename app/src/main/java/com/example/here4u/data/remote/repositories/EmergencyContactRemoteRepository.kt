package com.example.here4u.data.remote.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.here4u.data.local.entity.EmergencyContactEntity
import com.example.here4u.data.remote.entity.EmergencyContactRemote
import com.example.here4u.data.remote.entity.EmotionRemote
import javax.inject.Singleton

@Singleton
class EmergencyContactRemoteRepository @Inject constructor(
    private val userRepository: UserRemoteRepository
) {

    private val db = Firebase.firestore

    // 🔹 Agregar un contacto
    suspend fun addEmergencyContact(contact: EmergencyContactEntity): Boolean {
        return try {
            val userId = userRepository.getUserId() ?: return false

            val docRef = db.collection("emergency_contacts").document()
            val remoteContact = EmergencyContactRemote(
                documentId = docRef.id,
                userId = userId,
                name = contact.name,
                phone = contact.phone,
                email = contact.email,
                relation = contact.relation,

            )

            docRef.set(remoteContact).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // 🔹 Obtener todos los contactos de un usuario (reactivo con Flow)
    fun getAll(): Flow<List<EmergencyContactRemote>> = callbackFlow {
        val reg: ListenerRegistration =
            db.collection("EmergencyContact").whereEqualTo("userId", userRepository.getUserId())
                .addSnapshotListener { snap, err ->
                if (err != null) {
                    close(err); return@addSnapshotListener

                }
                val list = snap?.toObjects(EmergencyContactRemote::class.java) ?: emptyList()
                Log.d("FIREBASE", "Documentos recibidos: ${list.size}")
                trySend(list)
            }
        awaitClose { reg.remove() }
    }


    // 🔹 Obtener contactos del usuario actual (solo una vez, no en tiempo real)
    suspend fun getContactsForCurrentUser(): List<EmergencyContactRemote> {
        return try {
            val userId = userRepository.getUserId() ?: return emptyList()

            val snapshot = db.collection("emergency_contacts")
                .whereEqualTo("userID", userId)
                .get()
                .await()

            snapshot.toObjects(EmergencyContactRemote::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 🔹 Eliminar todos los contactos de un usuario
    suspend fun deleteContactsForCurrentUser() {
        val userId = userRepository.getUserId() ?: return
        val contacts = getContactsForCurrentUser()

        contacts.forEach { contact ->
            contact.documentId?.let { id ->
                db.collection("emergency_contacts").document(id).delete()
            }
        }
    }
}
