package com.example.here4u.data.repositories

import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

class JournalRepository (private val journalDao: JournalDao){

    suspend fun insert(entry: JournalEntity) {
        journalDao.insert(entry)
    }

    suspend fun delete(entry: JournalEntity) {
        journalDao.delete(entry)
    }

    fun getByDateRange(from: Long): Flow<List<JournalEntity>> {
        return journalDao.getSince(from,)
    }

    fun getByEmotion(entry: EmotionEntity): Flow<List<JournalEntity>> {
        return journalDao.getForEmotion(entry.id)}








}