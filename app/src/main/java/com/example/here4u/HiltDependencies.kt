package com.example.here4u


import android.content.Context
import com.example.here4u.data.local.database.db
import com.example.here4u.data.local.dao.EmotionDao
import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.repositories.emotions.EmotionRepository   // ← si tu repo está en com.example.here4u.repositories
import com.example.here4u.data.repositories.emotions.EmotionRepositoryFirebase
import com.example.here4u.data.repositories.emotions.EmotionSyncRepository
import com.example.here4u.data.repositories.journal.JournalRepository
import com.example.here4u.data.repositories.journal.JournalRepositoryFirebase
import com.example.here4u.data.repositories.journal.JournalSyncRepository
// si lo tienes en otro paquete, cámbialo: com.example.here4u.data.repositories.emotions.EmotionRepository, etc.

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): db = db.get(ctx)

    @Provides
    fun provideEmotionDao(database: db): EmotionDao = database.emotionDao()

    @Provides
    fun provideJournalDao(database: db): JournalDao = database.journalDao()

    @Provides @Singleton
    fun provideLocalJournalRepository(dao: JournalDao): JournalRepository = JournalRepository(dao)

    @Provides @Singleton
    fun provideJournalRepositoryFirebase(): JournalRepositoryFirebase {
        return JournalRepositoryFirebase();
    }

    @Provides @Singleton
    fun provideJournal(localrepository: JournalRepository,
                       remoteRepository: JournalRepositoryFirebase

    ): JournalSyncRepository {
        return JournalSyncRepository(localrepository,remoteRepository);
    }


    @Provides @Singleton
    fun provideLocalEmotionRepository(dao: EmotionDao): EmotionRepository = EmotionRepository(dao)

    @Provides @Singleton
    fun provideEmotionRepositoryFirebase(): EmotionRepositoryFirebase{
        return EmotionRepositoryFirebase()
    }
    @Provides
    fun provideEmotionRepository(
        localrepository: EmotionRepository,
        remoteRepository: EmotionRepositoryFirebase)
    :EmotionSyncRepository{
        return EmotionSyncRepository(localrepository, remoteRepository )
    }



}
