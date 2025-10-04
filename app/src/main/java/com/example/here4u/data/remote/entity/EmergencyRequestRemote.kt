package com.example.here4u.data.remote.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp


class EmergencyRequestRemote (@DocumentId val id: String? = null,
                              val userId: String = "", @ServerTimestamp val timestamp: Timestamp? = null,
                              val location: GeoPoint? = null,
                              val contacted: List<String> = emptyList()
    )
{}