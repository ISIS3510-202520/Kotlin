package com.example.here4u.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.here4u.data.local.repositories.JournalLocalRepository
import com.google.firebase.FirebaseApp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit


@HiltWorker
class SyncJournalWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: JournalLocalRepository
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {

        if (FirebaseApp.getApps(applicationContext).isEmpty()) {
            FirebaseApp.initializeApp(applicationContext)
            Log.d("WORKER", "🔥 Firebase inicializado manualmente")
        }

        Log.d("WORKER", "✅ Worker completado correctamente")
        repo.syncPendingJournals()
        Log.d("WORKER", "✅ Worker completado correctamente")
        Result.success()
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("WORKER", "❌ Error en Worker", e)
        Result.retry()
    }

    companion object{
        const val UNIQUE_NAME = "sync_journals"
        fun request(): OneTimeWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            return OneTimeWorkRequestBuilder<SyncJournalWorker>().setConstraints(constraints).setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                15, TimeUnit.SECONDS
            )
                .build()
        }

        }


}