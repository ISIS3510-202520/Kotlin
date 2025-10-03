package com.example.here4u.data.remote.dto

import com.google.firebase.firestore.DocumentReference
import org.jetbrains.annotations.NotNull

data class JournalDto(

    val id: String,
    val userId: String ,   // referencia directa
    val emotionId: String,
    val description: String?,
    val createdAt: Long?,
    val sharedWithTherapist: Boolean?
)
