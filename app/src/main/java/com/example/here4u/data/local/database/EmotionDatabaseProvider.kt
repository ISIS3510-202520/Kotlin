package com.example.here4u.data.local.database

import androidx.room.Room
import android.content.Context

object EmotionDatabaseProvider {

    // Instancia única de la base de datos
    @Volatile
    private var INSTANCE: EmotionDatabase? = null

    fun getDatabase(context: Context): EmotionDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                EmotionDatabase::class.java,
                "emotion_database"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
            INSTANCE = instance
            instance
        }
    }
}
