package com.example.here4u.data.remote.entity

import com.google.firebase.firestore.DocumentId

data class EmotionRemote(
    @DocumentId val id: String? = null,
    val name: String = "",
    val description: String = "",
    val colorHex: String = ""
)