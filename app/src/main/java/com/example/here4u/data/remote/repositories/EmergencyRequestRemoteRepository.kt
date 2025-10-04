package com.example.here4u.data.remote.repositories
import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.here4u.data.remote.entity.EmergencyRequestRemote
import com.example.here4u.data.remote.repositories.EmergencyContactRemoteRepository
import com.example.here4u.model.LocationModelImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class EmergencyRequestRemoteRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val locationProvider: LocationModelImpl,
    private val contactRepo: EmergencyContactRemoteRepository
) {
    @RequiresPermission(
        allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION]
    )
    suspend fun insert(requireLocation: Boolean = false): Result<String> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(IllegalStateException("No user logged in"))
            Log.d("FS", "UID=$uid")
            val loc = suspendCancellableCoroutine<android.location.Location?> { cont ->
                locationProvider.getCurrentLocation { location ->
                    cont.resume(location)
                }
            }

            val geo = loc?.let { GeoPoint(it.latitude, it.longitude) }
            if (requireLocation && geo == null) {
                return Result.failure(IllegalStateException("Location unavailable"))
            }

            val contacts = contactRepo.getAll().first()

            val emergency = EmergencyRequestRemote(
                userId = uid,
                location = geo,
                contacted = contacts.mapNotNull { it.documentId },
                timestamp = null
            )

            val ref = db.collection("emergencyRequests").add(emergency).await()
            Result.success(ref.id)

        } catch (e: Exception) {
            Log.e("EmergencyRepo", "Failed to insert emergency request", e)
            Result.failure(e)
        }
    }
}
