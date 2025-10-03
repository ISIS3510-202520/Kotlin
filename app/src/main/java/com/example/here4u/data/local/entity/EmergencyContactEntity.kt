package com.example.here4u.data.remote.entity

data class EmergencyContactEntity(
    val id: String = "",
    val userID: String = "",  // foreign key lógica
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val relation: String = "",
    val priority: String = ""
)
