package com.example.here4u.data.repositories.emotions

import com.example.here4u.data.local.dao.EmotionDao
import com.example.here4u.data.local.entity.EmotionEntity
import kotlinx.coroutines.flow.Flow

class EmotionRepository (private val emotionDao: EmotionDao){



    fun getAll(): Flow<List<EmotionEntity>> {
        return emotionDao.getAll()
    }

    suspend fun insertOne(item: EmotionEntity) = emotionDao.insertOne(item)



    suspend fun deleteById(id: String) = emotionDao.deleteById(id)


    suspend fun insertAll(list: List<EmotionEntity>) = emotionDao.insertAll(list)

    suspend fun upsertOne(item: EmotionEntity) = emotionDao.upsertOne(item)

    suspend fun getById(id: String): EmotionEntity? =
        emotionDao.getById(id)


}