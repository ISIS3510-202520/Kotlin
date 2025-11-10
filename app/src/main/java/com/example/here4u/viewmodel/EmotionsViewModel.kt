package com.example.here4u.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.local.cache.EmotionCache
import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.local.repositories.EmotionLocalRepository
import com.example.here4u.data.remote.entity.EmotionRemote
import com.example.here4u.data.remote.repositories.EmotionRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmotionsViewModel @Inject constructor(
    private val remoteRepository: EmotionRemoteRepository,
    private val localRepository: EmotionLocalRepository
) : ViewModel() {

    private val _emotions = MutableStateFlow<List<EmotionRemote>>(emptyList())
    val emotions: StateFlow<List<EmotionRemote>> = _emotions

    /**
     * Loads emotions with fallback logic:
     * Cache → Local (Room) → Remote (Firebase)
     */
    fun loadEmotions() {
        viewModelScope.launch {
            try {
                // 1️⃣ Try cache first
                EmotionCache.get()?.let {
                    _emotions.value = it
                    android.util.Log.d("EMOTIONS", "Loaded from cache (${it.size})")
                    return@launch
                }

                // 2️⃣ Try local database
                val localList = localRepository.getAll()
                if (localList.isNotEmpty()) {
                    val mapped = localList.map {
                        EmotionRemote(
                            name = it.name,
                            colorHex = it.color,
                            description = it.description
                        )
                    }
                    EmotionCache.save(mapped)
                    _emotions.value = mapped
                    android.util.Log.d("EMOTIONS", "Loaded from local DB (${mapped.size})")
                    return@launch
                }

                // 3️⃣ Fallback to remote
                val remoteList = remoteRepository.getAll().first()
                EmotionCache.save(remoteList)
                _emotions.value = remoteList

                // Optionally sync remote → local for next time
                val localEntities = remoteList.map {
                    EmotionEntity(
                        id = 0,
                        name = it.name,
                        color = it.colorHex,
                        description = it.description
                    )
                }
                localRepository.clearAll()
                localRepository.insertAll(localEntities)

                android.util.Log.d("EMOTIONS", "Loaded from remote (${remoteList.size})")
            } catch (e: Exception) {
                android.util.Log.e("EMOTIONS", "❌ Error loading emotions: ${e.message}")
            }
        }
    }

    fun clearCache() {
        EmotionCache.clear()
    }
}
