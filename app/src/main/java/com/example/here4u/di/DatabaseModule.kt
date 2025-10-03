package com.example.here4u.di

import android.app.Application
import com.example.here4u.data.local.database.db
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): db {
        return db.buildDatabase(app)
    }

    @Provides
    fun provideJournalDao(database: db) = database.journalDao()

    @Provides
    fun provideEmotionDao(database: db) = database.emotionDao()
}
