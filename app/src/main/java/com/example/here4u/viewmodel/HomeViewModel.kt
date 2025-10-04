package com.example.here4u.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DateFormat.getTimeInstance
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRemoteRepository,
    @ApplicationContext private val context: Context
): ViewModel(){

    private val mood= MutableStateFlow("")
    val moodText: StateFlow<String> = mood.asStateFlow()

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak.asStateFlow()

    init {
        refreshMoodText()
        startAutoUpdate()
        refreshUserStreak()

    }



    fun refreshMoodText(){
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

    val resId = when {
        hour < 12 -> "How are you feeling this morning?"    // 00:00–11:59
        hour < 18 -> "How are you feeling today?"     // 12:00–17:59
        else      -> "How are you feeling tonight?"     // 18:00–23:59
    }

        mood.value = resId

}

    fun refreshUserStreak() = viewModelScope.launch {
        val streakPair = userRepository.getUserStreak()
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




}