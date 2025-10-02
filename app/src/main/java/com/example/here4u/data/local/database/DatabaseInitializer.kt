package com.example.here4u.data.local.database
import com.example.here4u.data.local.SeedEmotions
import com.example.here4u.data.local.SeedJournals
import com.example.here4u.data.local.dao.EmotionDao
import com.example.here4u.data.local.dao.JournalDao
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class DatabaseInitializer @Inject constructor(
    private val emotionDao: EmotionDao,
    private val journalDao: JournalDao
) {
    suspend fun seed() {
        if (emotionDao.getAll().first().isEmpty()) {
            emotionDao.insertAll(SeedEmotions.list)
        }
        if (journalDao.getSince(0).first().isEmpty()) {
            journalDao.insertAll(SeedJournals.list)
        }
    }
}
