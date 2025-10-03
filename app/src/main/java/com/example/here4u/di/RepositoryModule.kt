package com.example.here4u.di
//di -> Dependencies injection
import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.remote.openai.OpenAIApi
import com.example.here4u.data.remote.openai.OpenAIClient
import com.example.here4u.data.repositories.JournalRepository
import com.example.here4u.data.repositories.RecapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.here4u.BuildConfig
import com.example.here4u.data.local.dao.EmotionDao

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideJournalRepository(journalDao: JournalDao): JournalRepository {
        return JournalRepository(journalDao)
    }

    @Provides
    @Singleton
    fun provideEmotionRepository(emotionDao: EmotionDao): EmotionRepository {
        return EmotionRepository(emotionDao)
    }

    @Provides
    @Singleton
    fun provideOpenAIApi(): OpenAIApi {
        val apiKey = BuildConfig.OPENAI_API_KEY
        android.util.Log.d("RepositoryModule", "API KEY from BuildConfig = '$apiKey'")
        return OpenAIClient.create(apiKey)
    }


    @Provides
    @Singleton
    fun provideRecapRepository(openAIApi: OpenAIApi): RecapRepository {
        return RecapRepository(openAIApi)
    }
}
