package com.example.here4u.di

import com.example.here4u.data.remote.openai.OpenAIApi
import com.example.here4u.data.remote.openai.OpenAIClient
import com.example.here4u.data.remote.repositories.EmotionRemoteRepository
import com.example.here4u.data.remote.repositories.JournalRemoteRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import com.example.here4u.data.remote.service.FirebaseService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.content.Context
import com.example.here4u.data.remote.repositories.EmergencyContactRemoteRepository
import com.example.here4u.data.remote.repositories.EmergencyRequestRemoteRepository
import com.example.here4u.model.LocationModelImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.here4u.BuildConfig


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {


    @Provides
    @Singleton
    fun provideFusedLocation(
        @ApplicationContext ctx: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(ctx)

    }

    @Provides
    @Singleton
    fun provideFirebaseService(): FirebaseService = FirebaseService()

    @Provides
    @Singleton
    fun provideEmergencyRequestRepository(serviceauth: FirebaseAuth, service: FirebaseFirestore, location: LocationModelImpl,
        contact: EmergencyContactRemoteRepository
    ,): EmergencyRequestRemoteRepository = EmergencyRequestRemoteRepository(service,serviceauth,location,contact)


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
    @Provides @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideOpenAIApi(): OpenAIApi {
        val apiKey = BuildConfig.OPENAI_API_KEY
        android.util.Log.d("RepositoryModule", "API KEY from BuildConfig = '$apiKey'")
        return OpenAIClient.create(apiKey)
    }



}
