package com.example.here4u.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.here4u.data.local.database.db
import com.example.here4u.data.repositories.EmotionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmotionsViewModel @Inject constructor(repository: EmotionRepository) : ViewModel()  {
    val emotions = repository.getAll().asLiveData()

}