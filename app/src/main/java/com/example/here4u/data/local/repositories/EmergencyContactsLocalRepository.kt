package com.example.here4u.data.local.repositories

import com.example.here4u.data.local.dao.EmergencyContactDao
import com.example.here4u.data.local.entity.EmergencyContactEntity
import com.example.here4u.data.mappers.toEntity
import com.example.here4u.data.remote.repositories.EmergencyContactRemoteRepository
import javax.inject.Inject

class EmergencyContactsLocalRepository @Inject constructor(private val emergencyContactDao: EmergencyContactDao) {

    suspend fun insert(emergencyContact: EmergencyContactEntity) {

        emergencyContactDao.insertEmergencyContact(emergencyContact)
    }

    suspend fun insert(emergencyContacts: List<EmergencyContactEntity>) {
        emergencyContactDao.insertAllEmergencyContacts(emergencyContacts)
    }

    suspend fun getAll(): List<EmergencyContactEntity> {
        return emergencyContactDao.getAllEmergencyContacts()
    }

    suspend fun syncPendingContacts(remoteRepository: EmergencyContactRemoteRepository) {
        val unsyncedContacts =remoteRepository.getContactsForCurrentUser()

        for (contact in unsyncedContacts) {
            try {

                val contactremote = contact.toEntity()
                insert(contactremote)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}