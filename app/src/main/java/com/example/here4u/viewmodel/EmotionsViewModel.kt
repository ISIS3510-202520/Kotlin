package com.example.here4u.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.local.repositories.EmotionLocalRepository

import com.example.here4u.data.remote.entity.EmotionRemote
import com.example.here4u.data.remote.repositories.EmotionRemoteRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class EmotionsViewModel @Inject constructor(
    private val repository: EmotionRemoteRepository,
    private val localRepository: EmotionLocalRepository
) : ViewModel() {



    val emotions: StateFlow<List<EmotionRemote>> =
        repository.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )



    fun syncEmotionsToLocal() {
        viewModelScope.launch {
            val remoteEmotions = emotions.value
            val localEmotions = localRepository.getAll()

            try {

                val remoteList = remoteEmotions


                val localList = remoteList.map {
                    EmotionEntity(
                        id = 0,
                        name = it.name,
                        color = it.colorHex,
                        description = it.description
                    )
                }

                localRepository.clearAll()
                localRepository.insertAll(localList)

                android.util.Log.d("LOCAL_DB", " ${localList.size} emotions loaded.")
            } catch (e: Exception) {
                android.util.Log.e("LOCAL_DB", " Error loading emotions: ${e.message}")
            }
        }
    }
}