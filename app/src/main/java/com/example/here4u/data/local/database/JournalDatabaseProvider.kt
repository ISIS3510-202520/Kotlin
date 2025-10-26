package com.example.here4u.data.local.database

import android.content.Context
import androidx.room.Room

object JournalDatabaseProvider {
    @Volatile
    private var INSTANCE: JournalDatabase? = null

    fun getDatabase(context: Context): JournalDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                JournalDatabase::class.java,
                "journal_database"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
            INSTANCE = instance
            instance
        }
    }
}
