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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
@HiltViewModel
class JournalingViewModel @Inject constructor(
    private val remoteRepository: JournalRemoteRepository,
    private val userRemoteRepository: UserRemoteRepository,
    private val localRepository: JournalLocalRepository,

    @ApplicationContext private val applicationContext: Context
) : ViewModel() {

    private val prefs = applicationContext.getSharedPreferences("user_cache", Context.MODE_PRIVATE)
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun saveText(emotionId: String, content: String) {

        applicationScope.launch(){
            try {

                    val time = remoteRepository.insertOne(emotionId, content)
                    localRepository.updateCache()
                    userRemoteRepository.updateLoginStreak()
                    userRemoteRepository.updateLastEntry(time)
                    Log.d("LOCAL_DB", "✅ Guardado remoto OK")



            } catch (e: Exception) {

                Log.e("LOCAL_DB", "❌ Error en la operación remota, guardando localmente.", e)

            }
        }
    }

    fun saveLocallyAndScheduleSync(emotionId: String, content: String) {

        applicationScope.launch(Dispatchers.IO){
            val userId = prefs.getString("uid",null)

            val journal = JournalEntity(
                emotionId = emotionId,
                description = content,
                createdAt = Timestamp.now(),
                userId = userId,
                sync = false
            )

            localRepository.insertJournal(journal)
            Log.d("LOCAL_DB", "Guardado local completado: $journal")



        }
        val workRequest = OneTimeWorkRequestBuilder<SyncJournalWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            SyncJournalWorker.UNIQUE_NAME,
            ExistingWorkPolicy.KEEP,
            workRequest
        )
        Log.d("LOCAL_DB", "WorkManager programado para sincronizar más tarde.")}
    }




