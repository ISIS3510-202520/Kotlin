package com.example.here4u.data.local.repositories

import com.example.here4u.data.local.dao.JournalDao
import com.example.here4u.data.local.entity.JournalEntity
import javax.inject.Inject



class JournalLocalRepository @Inject constructor(
    private val journalDao: JournalDao
) {
    suspend fun insertJournal(journal: JournalEntity) {
        journalDao.insertJournal(journal)
    }

    suspend fun getJournalsByUserId(userId: String?): List<JournalEntity> {
        return journalDao.getJournalsByUserId(userId)
    }
}
