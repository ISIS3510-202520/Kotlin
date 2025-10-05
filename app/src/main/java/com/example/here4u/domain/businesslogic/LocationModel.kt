package com.example.here4u.domain.businesslogic

import android.Manifest
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import javax.inject.Inject

class LocationModelImpl @Inject constructor(
    private val fusedClient: FusedLocationProviderClient
)  {

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentLocation(onResult: (Location?) -> Unit) {
        fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { loc ->
                onResult(loc)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}
