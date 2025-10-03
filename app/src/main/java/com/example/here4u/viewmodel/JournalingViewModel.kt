package com.example.here4u.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.repositories.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.asLiveData

@HiltViewModel
class JournalingViewModel @Inject constructor(
    private val repository: JournalRepository
) : ViewModel() {

    // Expose streak as LiveData so UI can observe
    val streak: LiveData<Int> = repository.getCurrentStreak()
        .asLiveData(viewModelScope.coroutineContext)
    fun saveText(emotionId: Long, content: String): Job =
        viewModelScope.launch {
            repository.addTextJournal(emotionId, content)
        }
    }

