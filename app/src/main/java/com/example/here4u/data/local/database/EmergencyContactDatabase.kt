package com.example.here4u.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.here4u.data.local.dao.EmergencyContactDao
import com.example.here4u.data.local.entity.EmergencyContactEntity
@Database(
    entities = [EmergencyContactEntity::class],
    version = 1, exportSchema = false)
abstract class EmergencyContactDatabase:RoomDatabase() {
    abstract fun emergencyContactDao(): EmergencyContactDao
}



