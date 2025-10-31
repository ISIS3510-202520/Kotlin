package com.example.here4u.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.here4u.data.local.entity.JournalEntity
import com.example.here4u.data.local.repositories.JournalLocalRepository
import com.example.here4u.data.remote.repositories.JournalRemoteRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalingViewModel @Inject constructor(
    private val repository: JournalRemoteRepository,
    private val repositoryuser: UserRemoteRepository,
    private val localRepository: JournalLocalRepository
) : ViewModel() {

    fun saveText(emotionId: String, content: String): Job =
        viewModelScope.launch {
            try {
                Log.d("LOCAL_DB", "🧠 Iniciando guardado remoto...")
                val time: Timestamp? = repository.insertOne(emotionId, content)
                Log.d("LOCAL_DB", "✅ Guardado remoto completado: $time")

                repositoryuser.updateLoginStreak()
                repositoryuser.updateLastEntry(time)

                // Crear Journal para guardar localmente
                val localTimestamp = time ?: Timestamp.now()
                val journal = JournalEntity(
                    emotionId = emotionId,
                    description = content,
                    createdAt = localTimestamp,
                    userId = repositoryuser.getUserId()
                )

                Log.d("LOCAL_DB", "💾 Intentando guardar en local: $journal")
                localRepository.insertJournal(journal)
                Log.d("LOCAL_DB", "✅ Journal guardado localmente con éxito")
                val journals = localRepository.getJournalsByUserId(repositoryuser.getUserId())
                Log.d("LOCAL_DB", "📋 Journals guardados: $journals")

            } catch (e: Exception) {
                Log.e("LOCAL_DB", "❌ Error durante el guardado local o remoto", e)
            }
        }
}
