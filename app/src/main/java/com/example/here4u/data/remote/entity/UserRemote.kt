package com.example.here4u.data.remote.entity

import com.google.firebase.Timestamp

data class UserRemote(
    val id: String = "",
    val displayName: String = "",
    val email: String = "",
    val createdAt: Timestamp? =null,
    val lastLogin: Timestamp? =null,
    val lastEntryDate: Timestamp?=null,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)
