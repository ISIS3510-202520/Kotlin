package com.example.here4u.data.remote.repositories

import android.util.Log
import com.example.here4u.BuildConfig
import com.example.here4u.data.local.entity.EmergencyContactEntity
import com.example.here4u.data.remote.entity.EmergencyContactRemote
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmergencyContactRemoteRepository @Inject constructor(
    private val userRepository: UserRemoteRepository
) {

    private val db = Firebase.firestore
    suspend fun addEmergencyContact(contact: EmergencyContactEntity): Boolean {
        return try {
            val userId = userRepository.getUserId() ?: return false

            val docRef = db.collection("EmergencyContact").document()
            val remoteContact = EmergencyContactRemote(
                documentId = docRef.id,
                userId = userId,
                name = contact.name,
                phone = contact.phone,
                email = contact.email,
                relation = contact.relation
            )

            docRef.set(remoteContact).await()
            Log.d("EmergencyRepo", "Contacto agregado correctamente")
            true
        } catch (e: Exception) {
            Log.e("EmergencyRepo", "Error agregando contacto: ${e.message}", e)
            false
        }
    }

    fun getAll(): Flow<List<EmergencyContactRemote>> = callbackFlow {
        val userId = userRepository.getUserId()
        if (userId == null) {
            close(IllegalStateException("User ID is null"))
            return@callbackFlow
        }

        val registration: ListenerRegistration =
            db.collection("EmergencyContact")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("EmergencyRepo", "Error en snapshot: ${error.message}", error)
                        close(error)
                        return@addSnapshotListener
                    }

                    val contacts = snapshot?.toObjects(EmergencyContactRemote::class.java) ?: emptyList()
                    Log.d("EmergencyRepo", "Snapshot actualizado: ${contacts.size} contactos obtenidos")
                    trySend(contacts).isSuccess
                }

        awaitClose { registration.remove() }
    }

    suspend fun getContactsForCurrentUser(): List<EmergencyContactRemote> {
        return try {
            val userId = userRepository.getUserId() ?: return emptyList()

            val snapshot = db.collection("EmergencyContact")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val contacts = snapshot.toObjects(EmergencyContactRemote::class.java)
            Log.d("EmergencyRepo", "${contacts.size} contactos encontrados para el usuario")
            contacts
        } catch (e: Exception) {
            Log.e("EmergencyRepo", "Error obteniendo contactos: ${e.message}", e)
            emptyList()
        }
    }


    suspend fun deleteContactsForCurrentUser() {
        try {
            val userId = userRepository.getUserId() ?: return
            val contacts = getContactsForCurrentUser()

            for (contact in contacts) {
                contact.documentId?.let { id ->
                    db.collection("EmergencyContact").document(id).delete().await()
                    Log.d("EmergencyRepo", "Contacto $id eliminado")
                }
            }
        } catch (e: Exception) {
            Log.e("EmergencyRepo", "Error eliminando contactos: ${e.message}", e)
        }
    }

    fun notifyAllContacts(locationMessage: String) {
        val username = BuildConfig.EMAIL_USERNAME
        val appPassword = BuildConfig.EMAIL_APP_PASSWORD

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val contacts = getContactsForCurrentUser()
                if (contacts.isEmpty()) {
                    Log.w("EmergencyRepo", "No hay contactos registrados para enviar correos")
                    return@launch
                }

                val senderName = userRepository.getName()
                var successCount = 0

                for (contact in contacts) {
                    val success = EmailSender.sendEmail(
                        recipientEmail = contact.email,
                        recipientName = contact.name,
                        locationMessage = locationMessage,
                        senderName = senderName,
                        username = username,
                        appPassword = appPassword
                    )

                    if (success) successCount++
                }

                Log.d(
                    "EmergencyRepo",
                    " $successCount / ${contacts.size} correos enviados correctamente"
                )
            } catch (e: Exception) {
                Log.e("EmergencyRepo", "Error enviando correos: ${e.message}", e)
            }
        }
    }
}