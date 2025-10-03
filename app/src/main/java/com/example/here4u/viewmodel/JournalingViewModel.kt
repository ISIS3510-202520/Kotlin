package com.example.here4u.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.repositories.journal.JournalRepository
import com.example.here4u.data.repositories.journal.JournalSyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalingViewModel @Inject constructor(
    private val repository: JournalSyncRepository,

) : ViewModel() {
    private val userId="cOWNeDr4c4egy5hrgt37qMZ5r3G3"
    fun saveText( emotionId: String, content: String): Job =
        viewModelScope.launch {
            repository.addJournal(emotionId,userId, content)
        }
    }

