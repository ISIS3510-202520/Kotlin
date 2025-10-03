package com.example.here4u.data.remote.service

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseService(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val emotions = db.collection("emotions")
    val journals = db.collection("journals")
    val users = db.collection("users")
}
