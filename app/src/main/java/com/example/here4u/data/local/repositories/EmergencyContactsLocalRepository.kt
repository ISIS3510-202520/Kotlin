package com.example.here4u.data.local.repositories

import com.example.here4u.data.local.dao.EmergencyContactDao
import com.example.here4u.data.local.entity.EmergencyContactEntity
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


}