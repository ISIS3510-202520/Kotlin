package com.example.here4u.data.local.repositories

import com.example.here4u.data.local.dao.EmotionDao
import com.example.here4u.data.local.entity.EmotionEntity
import javax.inject.Inject

class EmotionLocalRepository @Inject
    constructor(private val emotionDao: EmotionDao) {

         suspend fun insert( emotion : EmotionEntity) {
             emotionDao.insertEmotion(emotion)
         }
        suspend fun insertAll(emotions: List<EmotionEntity>) {
            emotionDao.insertAll(emotions)
        }
         suspend fun getAll(): List<EmotionEntity> {
             return emotionDao.getAll()
         }
         suspend fun clearAll(){
             emotionDao.clearAll()

         }

}