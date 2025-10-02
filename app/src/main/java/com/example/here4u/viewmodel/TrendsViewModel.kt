package com.example.here4u.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.repositories.JournalRepository
import com.example.here4u.data.repositories.RecapRepository
import com.example.here4u.model.Journal
import com.example.here4u.model.Recap
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrendsViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val recapRepository: RecapRepository
) : ViewModel(){

    private val _recap = MutableLiveData<Recap>()
    val recap: LiveData<Recap> get() = _recap

    fun loadWeeklyRecap() {
        viewModelScope.launch {
            Log.d("TrendsVM", "Launching collect for last7days")
            try {
                journalRepository.getLast7Days().collectLatest { journals: List<Journal> ->
                    Log.d("TrendsVM", "Flow emitted ${journals.size} journals")
                    if (journals.isNotEmpty()) {
                        try {
                            val recapData = recapRepository.generateRecapWithAI(journals)
                            Log.d("TrendsVM", "Recap trendPoints count = ${recapData.trendPoints.size}")
                            _recap.postValue(recapData)
                        } catch (e: Exception) {
                            Log.e("TrendsVM", "Error generating recap with AI", e)
                            _recap.postValue(
                                Recap(
                                    highlights = listOf("Error generating recap"),
                                    summary = "Please try again later.",
                                    trendPoints = emptyList()
                                )
                            )
                        }
                    } else {
                        _recap.postValue(
                            Recap(
                                highlights = listOf("No journals this week"),
                                summary = "Try writing your thoughts daily to see your weekly trends.",
                                trendPoints = emptyList()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("TrendsVM", "Error collecting Flow", e)
            }
        }
    }
}
