package com.example.here4u.data.local.database

import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.local.entity.JournalEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.here4u.data.local.converter.JournalConverter

@Database(entities = [JournalEntity::class], version = 1, exportSchema = false)
@TypeConverters(JournalConverter::class)
abstract class JournalDatabase: RoomDatabase() {
    abstract fun journalDao(): JournalDao
}