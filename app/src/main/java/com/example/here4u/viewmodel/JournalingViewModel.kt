package com.example.here4u.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.remote.repositories.JournalRemoteRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import com.google.firebase.Timestamp

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalingViewModel @Inject constructor(
   private val repository: JournalRemoteRepository,
    private val repositoryuser: UserRemoteRepository
) : ViewModel() {

    fun saveText(emotionId: String, content: String): Job =
        viewModelScope.launch {
            val time: Timestamp? = repository.insertOne(emotionId,content)
            repositoryuser.updateLoginStreak()
            repositoryuser.updateLastEntry(time)


        }
    }

