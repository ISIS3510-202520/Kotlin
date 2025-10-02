package com.example.here4u.data.repositories

import com.example.here4u.data.local.dao.EmotionDao
import com.example.here4u.data.local.entity.EmotionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmotionRepository @Inject constructor (private val emotionDao: EmotionDao){


    fun getAll(): Flow<List<EmotionEntity>> {
        return emotionDao.getAll()
    }

     suspend fun insertAllEmotions(emotions: List<EmotionEntity>) {
        emotionDao.insertAll(emotions)
    }

}