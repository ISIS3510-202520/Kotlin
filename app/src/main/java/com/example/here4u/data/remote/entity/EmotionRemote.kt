package com.example.here4u.data.remote.entity

data class EmotionRemote(
    val id: String = "", // Firestore doesn't auto include ID in object
    val name: String = "",
    val description: String = "",
    val colorHex: String = ""
)