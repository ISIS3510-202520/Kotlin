package com.example.here4u.data.remote.entity

import com.google.firebase.firestore.DocumentId

data class EmergencyContactRemote(
    @DocumentId val documentId: String? = null, // ← ID del documento en Firestore
    val userId: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val relation: String = ""
)
