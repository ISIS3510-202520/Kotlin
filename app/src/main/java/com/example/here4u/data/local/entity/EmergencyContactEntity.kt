package com.example.here4u.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EmergencyContacts_table")
data class EmergencyContactEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val documentId: String? = null,
    val userId: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val relation: String = ""
)
