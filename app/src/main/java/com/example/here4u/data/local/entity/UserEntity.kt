package com.example.here4u.model.entity

import com.google.firebase.Timestamp

data class UserEntity(
    var id: String = "",
    var displayName: String = "",
    var email: String = "",
    var createdAt: Timestamp? = null,
    var lastLogin: Timestamp? = null,
    var lastEntryDate: Timestamp? = null,  // 🔸 Esta NO se actualiza automáticamente
    var currentStreak: Int = 0,
    var longestStreak: Int = 0
) {
    // 🔸 Constructor vacío requerido por Firebase para la deserialización
    constructor() : this(
        "",
        "",
        "",
        null,
        null,
        null,
        0,
        0
    )
}
