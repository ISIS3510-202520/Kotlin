package com.example.here4u.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.here4u.data.local.cache.RecapCache
import com.example.here4u.data.local.dao.EmergencyContactDao
import com.example.here4u.data.local.dao.EmotionDao
import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.local.database.EmergencyContactDatabase
import com.example.here4u.data.local.database.EmotionDatabase
import com.example.here4u.data.local.database.JournalDatabase
import com.example.here4u.data.local.repositories.EmergencyContactsLocalRepository
import com.example.here4u.data.local.repositories.EmotionLocalRepository
import com.example.here4u.data.local.repositories.JournalLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // ---------------------- JOURNAL DATABASE ----------------------

    @Provides
    @Singleton
    fun provideJournalDatabase(@ApplicationContext appContext: Context): JournalDatabase {
        Log.d("DB_PROVIDER", "🧱 Creando instancia de RoomDatabase")
        return Room.databaseBuilder(
            appContext,
            JournalDatabase::class.java,
            "journal_database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideJournalDao(database: JournalDatabase): JournalDao =
        database.journalDao()




    // ---------------------- EMOTION DATABASE ----------------------

    @Provides
    @Singleton
    fun provideEmotionDatabase(@ApplicationContext appContext: Context): EmotionDatabase {
        return Room.databaseBuilder(
            appContext,
            EmotionDatabase::class.java,
            "emotion_database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideEmotionDao(database: EmotionDatabase): EmotionDao =
        database.emotionDao()

    @Provides
    fun provideEmotionLocalRepository(dao: EmotionDao): EmotionLocalRepository =
        EmotionLocalRepository(dao)

    // ------------------- EMERGENCY CONTACT DATABASE -------------------

    @Provides
    @Singleton
    fun provideEmergencyContactDatabase(@ApplicationContext appContext: Context): EmergencyContactDatabase {
        return Room.databaseBuilder(
            appContext,
            EmergencyContactDatabase::class.java,
            "emergency_contacts_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideEmergencyContactDao(database: EmergencyContactDatabase): EmergencyContactDao =
        database.emergencyContactDao()

    @Provides
    fun provideEmergencyContactsLocalRepository(dao: EmergencyContactDao): EmergencyContactsLocalRepository =
        EmergencyContactsLocalRepository(dao)

    @Provides
    @Singleton
    fun provideRecapCache(): RecapCache = RecapCache()

}
