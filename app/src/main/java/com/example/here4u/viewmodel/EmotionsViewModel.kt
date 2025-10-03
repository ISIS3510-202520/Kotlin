package com.example.here4u.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.here4u.data.local.database.db
import com.example.here4u.data.remote.entity.EmotionRemote
import com.example.here4u.data.remote.repositories.EmotionRemoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmotionsViewModel @Inject constructor(
    private val repository: EmotionRemoteRepository
) : ViewModel() {

    // Emite en tiempo real la lista remota
    val emotions: StateFlow<List<EmotionRemote>> =
        repository.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}