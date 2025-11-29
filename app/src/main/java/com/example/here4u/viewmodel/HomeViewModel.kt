package com.example.here4u.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.local.entity.JournalEntity
import com.example.here4u.data.local.repositories.JournalLocalRepository
import com.example.here4u.data.local.repositories.RecapLocalRepository
import com.example.here4u.data.remote.repositories.RecapRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.DateFormat.getTimeInstance
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRemoteRepository,
    private val localRepo: JournalLocalRepository,
    private val recapLocalRepository: RecapLocalRepository,
    @ApplicationContext private val context: Context
): ViewModel(){

    private val mood= MutableStateFlow("")
    val moodText: StateFlow<String> = mood.asStateFlow()

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak.asStateFlow()

    private val _lastFive = MutableLiveData<List<JournalEntity>>()
    val lastFive: LiveData<List<JournalEntity>> get() = _lastFive

    private val _lastPdf = MutableLiveData<File?>()
    val lastPdf: LiveData<File?> = _lastPdf



    init {
        refreshMoodText()
        startAutoUpdate()
        refreshUserStreak()

        viewModelScope.launch {
            localRepo.cacheFlow.collect { cached ->
                _lastFive.postValue(cached)
            }


        }

        viewModelScope.launch(Dispatchers.IO) {
            localRepo.updateCache()
        }
    }



    suspend fun updatecache(){
        localRepo.updateCache()
    }

    fun updatelastfive(){
        _lastFive.value = localRepo.getCachedJournals()
    }

    fun refreshMoodText(){

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

    val resId = when {
        hour < 12 -> "How are you feeling this morning?"
        hour < 18 -> "How are you feeling today?"
        else      -> "How are you feeling tonight?"
    }

        mood.value = resId

}
    fun refreshUserStreak() = viewModelScope.launch {

        val streakPair = withContext(Dispatchers.IO) {
            userRepository.getUserStreak()
        }
        streakPair?.let {
            _streak.value = it.first
        }
    }

    private fun startAutoUpdate() = viewModelScope.launch {
        while (isActive) {
            val delayMs = millisUntilNextBoundary()
            kotlinx.coroutines.delay(delayMs)
            refreshMoodText()
        }
    }
    private fun millisUntilNextBoundary(): Long {
        val now = Calendar.getInstance()
        val next = (now.clone() as Calendar).apply {
            when {
                now.get(Calendar.HOUR_OF_DAY) < 12 -> set(Calendar.HOUR_OF_DAY, 12)
                now.get(Calendar.HOUR_OF_DAY) < 18 -> set(Calendar.HOUR_OF_DAY, 18)
                else -> { add(Calendar.DAY_OF_YEAR, 1); set(Calendar.HOUR_OF_DAY, 0) }
            }
            set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        return (next.timeInMillis - now.timeInMillis).coerceAtLeast(1_000L)
    }

    fun getDocument(){
        val file = recapLocalRepository.getLastSavedPdf()
        _lastPdf.postValue(file)

    }




}