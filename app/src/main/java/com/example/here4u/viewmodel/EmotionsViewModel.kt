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
     * Loads emotions with continuous updates:
     * Cache → Local (Room) → Remote (Firestore Flow)
     */
    fun loadEmotions() {
        viewModelScope.launch {
            try {
                // 1️⃣ Try cache first (instant display)
                EmotionCache.get()?.let {
                    _emotions.value = it
                    android.util.Log.d("EMOTIONS", "Loaded from cache (${it.size})")
                }

                // 2️⃣ Try local database if cache empty or outdated
                if (_emotions.value.isEmpty()) {
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
                    }
                }

                // 3️⃣ Always start collecting remote flow (continuous sync)
                remoteRepository.getAll().collect { remoteList ->
                    if (remoteList.isNotEmpty()) {
                        EmotionCache.save(remoteList)
                        _emotions.value = remoteList

                        // Keep local DB in sync for offline mode
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

                        android.util.Log.d("EMOTIONS", "🔥 Synced from remote (${remoteList.size})")
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("EMOTIONS", "❌ Error loading emotions: ${e.message}")
            }
        }
    }

    fun clearCache() {
        EmotionCache.clear()
    }
}
