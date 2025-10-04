package com.example.here4u.data.remote.entity

import com.google.firebase.firestore.DocumentId

data class EmergencyContactRemote(
    @DocumentId val id: String? = null,   // Se completa con el id del documento en Firestore
    val userId: String = "",              // El id del usuario dueño de este contacto
    val name: String = "",                // Nombre del contacto
    val phone: String = "",               // Teléfono
    val email: String = "",               // Email
    val relation: String = ""             // Relación (ej: Madre, Hermano, Amigo)
)