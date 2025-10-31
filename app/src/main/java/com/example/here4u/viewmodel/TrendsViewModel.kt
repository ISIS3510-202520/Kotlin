package com.example.here4u.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.remote.repositories.JournalRepository
import com.example.here4u.data.remote.repositories.RecapRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import com.example.here4u.domain.model.Journal
import com.example.here4u.domain.model.Recap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.here4u.data.local.repositories.RecapLocalRepository
import com.sun.activation.registries.LogSupport.log
import java.io.File
import kotlin.math.log

// Threads todo

@HiltViewModel
class TrendsViewModel @Inject constructor(
    private val journalRepository: JournalRepository,
    private val recapRepository: RecapRepository,
    private val userRemoteRepository: UserRemoteRepository,
    private val recapLocalRepository: RecapLocalRepository
) : ViewModel() {



    private val _recap = MutableLiveData<Recap>()
    val recap: LiveData<Recap> get() = _recap
    private val _lastPdf = MutableLiveData<File?>()
    val lastPdf: LiveData<File?> = _lastPdf

    fun loadWeeklyRecap() {
        viewModelScope.launch {
            val userId = userRemoteRepository.getUserId()

            if (userId == null) {
                Log.e("TrendsVM", "No user logged in — cannot fetch journals.")
                _recap.postValue(
                    Recap(
                        highlights = listOf("User not authenticated"),
                        summary = "Please log in to view your weekly recap."
                    )
                )
                return@launch
            }

            Log.d("TrendsVM", "Fetching journals for userId=$userId")

            try {
                journalRepository.getLast7Days(userId).collectLatest { journals: List<Journal> ->
                    Log.d("TrendsVM", "Flow emitted ${journals.size} journals")
                    if (journals.isNotEmpty()) {
                        try {
                            val recapData = recapRepository.generateRecapWithAI(userId,journals)
                            Log.d(
                                "TrendsVM",
                                "Recap received: highlights=${recapData.highlights.size}, summary length=${recapData.summary.length}"
                            )
                            _recap.postValue(recapData)

                            val verdad=recapLocalRepository.shouldSaveSummary()
                            log("$verdad")
                            if (verdad){
                                log("entró")
                                recapLocalRepository.saveSummary(recapData.summary)
                                recapLocalRepository.updateLastSaveTime()

                            }
                        } catch (e: Exception) {
                            Log.e("TrendsVM", "Error generating recap with AI", e)
                            _recap.postValue(
                                Recap(
                                    highlights = listOf("Error generating recap"),
                                    summary = "Please try again later."
                                )
                            )
                        }
                    } else {
                        _recap.postValue(
                            Recap(
                                highlights = listOf("No journals this week"),
                                summary = "Try writing your thoughts daily to see your weekly recap."
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("TrendsVM", "Error collecting Flow", e)
            }
        }
    }

    fun getDocument(){
        val file = recapLocalRepository.getLastSavedPdf()
        _lastPdf.postValue(file)

    }
}
