package com.example.here4u.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.here4u.data.local.entity.JournalEntity
import com.example.here4u.data.local.repositories.JournalLocalRepository
import com.example.here4u.data.remote.repositories.JournalRemoteRepository
import com.example.here4u.data.remote.repositories.UserRemoteRepository
import com.example.here4u.utils.NetworkUtils
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.ExistingWorkPolicy

import com.example.here4u.data.worker.SyncJournalWorker
import com.google.type.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

@HiltViewModel
class JournalingViewModel @Inject constructor(
    private val repository: JournalRemoteRepository,
    private val repositoryuser: UserRemoteRepository,
    private val localRepository: JournalLocalRepository
) : ViewModel() {

    fun saveText(emotionId: String, content: String, context: Context): Job =
        viewModelScope.launch(Dispatchers.IO) {
            var sync = false
            var time: Timestamp? = null

            try {
                time = repository.insertOne(emotionId, content)
                repositoryuser.updateLoginStreak()
                repositoryuser.updateLastEntry(time)
                sync = true
                Log.d("LOCAL_DB", "✅ Guardado remoto OK")

            } catch (e: Exception) {
                Log.e("LOCAL_DB", "❌ Error remoto", e)
                if (!NetworkUtils.isNetworkAvailable(context.applicationContext)) {
                    WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
                        SyncJournalWorker.UNIQUE_NAME,
                        ExistingWorkPolicy.KEEP,
                        SyncJournalWorker.request()
                    )
                    Log.d("LOCAL_DB", "WorkManager programado para reintentar")
                }
            } finally {

                withContext(NonCancellable) {
                    val localTimestamp = time ?: Timestamp.now()
                    val journal = JournalEntity(
                        emotionId = emotionId,
                        description = content,
                        createdAt = localTimestamp,
                        userId = repositoryuser.getUserId(),
                        sync = sync
                    )

                    localRepository.insertJournal(journal)
                    Log.d("LOCAL_DB", "Guardado local completado: $journal")
                }
            }
        }
}

