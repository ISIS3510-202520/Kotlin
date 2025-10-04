package com.example.here4u.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EmergencyContacts_table")
data class EmergencyContactEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0, // ID autogenerado solo para Room

    @ColumnInfo(name = "documentId")
    val documentId: String? = null, // ID del documento en Firestore

    @ColumnInfo(name = "userId")
    val userId: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "phone")
    val phone: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "relation")
    val relation: String = ""
)
