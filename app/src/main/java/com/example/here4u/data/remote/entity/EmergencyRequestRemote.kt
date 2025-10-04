package com.example.here4u.data.remote.entity

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import com.google.type.Date

class EmergencyRequestRemote (@DocumentId val id: String? = null,
                              val userId: String = "", @ServerTimestamp val timestamp: Date? = null,
                              val location: GeoPoint? = null,
                              val contacted: List<String> = emptyList()
    )
{}