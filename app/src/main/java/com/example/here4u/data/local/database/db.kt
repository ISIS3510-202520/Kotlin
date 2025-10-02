package com.example.here4u.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.here4u.data.local.dao.EmotionDao
import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.local.entity.EmotionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.here4u.data.local.SeedEmotions
import com.example.here4u.data.local.entity.JournalEntity

@Database(entities = [EmotionEntity::class, JournalEntity::class], version = 2, exportSchema = false)
abstract class db : RoomDatabase() {

    abstract fun emotionDao(): EmotionDao
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile private var INSTANCE: db? = null
        private const val DATABASE_NAME = "app.db"

        fun get(context: Context): db {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    db::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Insert emotions when the DB is created
                            CoroutineScope(Dispatchers.IO).launch {
                                get(context).emotionDao().insertAll(SeedEmotions.list)
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
