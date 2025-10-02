package com.example.here4u.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.here4u.data.local.SeedEmotions
import com.example.here4u.data.local.SeedJournals
import com.example.here4u.data.local.dao.EmotionDao
import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.local.entity.JournalEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [EmotionEntity::class, JournalEntity::class],
    version = 2,
    exportSchema = false
)
abstract class db : RoomDatabase() {

    abstract fun emotionDao(): EmotionDao
    abstract fun journalDao(): JournalDao

    companion object {
        private const val DATABASE_NAME = "app.db"

        fun buildDatabase(context: Context): db {
            return Room.databaseBuilder(
                context.applicationContext,
                db::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Launch seeding in background
                        CoroutineScope(Dispatchers.IO).launch {
                            // 🚨 IMPORTANT: You can’t call db.emotionDao() directly here,
                            // because this db parameter is SupportSQLiteDatabase, not RoomDatabase.
                            // Instead, let Hilt inject db and seed from a Repository/Initializer.
                        }
                    }
                })
                .build()
        }
    }
}
