package com.example.here4u.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.here4u.data.local.entity.EmergencyContactEntity

@Dao
interface EmergencyContactDao {
    @Query("SELECT * FROM EmergencyContacts_table")
    suspend fun getAllEmergencyContacts(): List<EmergencyContactEntity>

    @Query("SELECT * FROM EmergencyContacts_table WHERE documentId = :documentId")
    suspend fun getEmergencyContactByDocumentId(documentId: String): EmergencyContactEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyContact(emergencyContact: EmergencyContactEntity)

    @Insert
    suspend fun insertAllEmergencyContacts(emergencyContacts: List<EmergencyContactEntity>)

    @Query("DELETE FROM EmergencyContacts_table")
    suspend fun clearAll()

}