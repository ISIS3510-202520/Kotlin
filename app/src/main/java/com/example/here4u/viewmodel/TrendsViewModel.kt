package com.example.here4u.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.here4u.model.repositories.JournalRepository
import com.example.here4u.model.Recap
import com.example.here4u.model.RecapGenerator

class TrendsViewModel(private val repository: JournalRepository) : ViewModel() {

    private val _recap = MutableLiveData<Recap>()
    val recap: LiveData<Recap> get() = _recap

    fun loadMonthlyRecap() {
        val journals = repository.getJournalsForLastMonth()
        val recapData = RecapGenerator.generate(journals) // step 4
        _recap.value = recapData
    }
}
