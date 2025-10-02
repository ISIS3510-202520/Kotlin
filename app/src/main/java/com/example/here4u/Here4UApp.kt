package com.example.here4u

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.here4u.data.local.database.DatabaseInitializer

@HiltAndroidApp
class Here4UApp : Application() {
    @Inject lateinit var initializer: DatabaseInitializer

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            initializer.seed()
        }
    }
}

