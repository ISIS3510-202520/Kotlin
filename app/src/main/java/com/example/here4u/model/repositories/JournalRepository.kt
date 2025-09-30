package com.example.here4u.model.repositories
import com.example.here4u.model.Journal

interface JournalRepository { fun getJournalsForLastMonth(): List<Journal> fun saveJournal(journal: Journal) }