package com.example.here4u.di

import com.example.here4u.data.remote.openai.OpenAIApi
import com.example.here4u.data.remote.openai.OpenAIClient
import com.example.here4u.data.remote.repositories.EmotionRemoteRepository
import com.example.here4u.data.remote.repositories.JournalRemoteRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import com.example.here4u.data.remote.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.here4u.BuildConfig


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseService(): FirebaseService = FirebaseService()


    @Provides
    @Singleton
    fun provideEmotionRepository(): EmotionRemoteRepository =
        EmotionRemoteRepository()

    @Provides
    @Singleton
    fun provideUserRepository(service: FirebaseAuth): UserRemoteRepository =
        UserRemoteRepository(service)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
        }

    @Provides
    @Singleton
    fun provideOpenAIApi(): OpenAIApi {
        val apiKey = BuildConfig.OPENAI_API_KEY
        android.util.Log.d("RepositoryModule", "API KEY from BuildConfig = '$apiKey'")
        return OpenAIClient.create(apiKey)
    }
}
