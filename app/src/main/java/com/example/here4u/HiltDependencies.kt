package com.example.here4u

import com.example.here4u.data.local.dao.EmotionDao
import com.example.here4u.data.repositories.EmotionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

//    @Provides
//    @Singleton
//    fun provideEmotionRepository(dao: EmotionDao): EmotionRepository =
//        EmotionRepository(dao)
}
