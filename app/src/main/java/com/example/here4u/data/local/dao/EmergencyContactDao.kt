package com.example.here4u.data.local.dao

import android.R
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.here4u.data.local.entity.EmergencyContactEntity

@Dao
interface EmergencyContactDao {
    @Query("SELECT * FROM EmergencyContacts_table")
    suspend fun getAllEmergencyContacts(): List<EmergencyContactEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyContact(emergencyContact: EmergencyContactEntity)

    @Insert
    suspend fun insertAllEmergencyContacts(emergencyContacts: List<EmergencyContactEntity>)

    @Query("DELETE FROM EmergencyContacts_table")
    suspend fun clearAll()

    @Query("SELECT * FROM EmergencyContacts_table WHERE synced = 0")
    suspend fun getUnsyncedContacts(): List<EmergencyContactEntity>

    @Query("UPDATE EmergencyContacts_table SET synced = :state WHERE localId =:id")
    suspend fun updateSync(id: Long, state: Boolean)

    @Query("DELETE FROM EmergencyContacts_table")
    suspend fun eraseAll()

}