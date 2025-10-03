package com.example.here4u.data.remote.dto

data class EmergencyRequestDto(
    val id: String?,
    val userId: String?,
    val timestamp: Long?,
    val location: Map<String, Double> = mapOf("lat" to 0.0, "lng" to 0.0),
    val contacted: List<String> = emptyList() // IDs de EmergencyContact
)