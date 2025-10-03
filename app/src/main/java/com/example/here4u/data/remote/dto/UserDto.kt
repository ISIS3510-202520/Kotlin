package com.example.here4u.data.remote.dto

data class UserDto(
    val uid: String ?,
    val email: String ?,
    val displayName: String?,
    val lastLogin: Long?,
    val currentStreak: Int?,
    val longestStreak: Int ?,
    val lastEntryDate: Long ?
)