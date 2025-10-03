package com.example.here4u.data.remote.dto

import com.google.firebase.firestore.DocumentReference

data class EmergencyContactDto(
    val id: String,
    val userId: DocumentReference?,
    val name: String?,
    val phone: String?,
    val email: String?,
    val relation: String?
)