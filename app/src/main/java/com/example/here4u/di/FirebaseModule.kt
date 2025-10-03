package com.example.here4u.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseService(): FirebaseService = FirebaseService()

    @Provides
    @Singleton
    fun provideEmotionRepository(service: FirebaseService): EmotionRemoteRepository =
        EmotionRemoteRepository(service)

    @Provides
    @Singleton
    fun provideJournalRepository(service: FirebaseService): JournalRemoteRepository =
        JournalRemoteRepository(service)

    @Provides
    @Singleton
    fun provideUserRepository(service: FirebaseService): UserRemoteRepository =
        UserRemoteRepository(service)

    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}
