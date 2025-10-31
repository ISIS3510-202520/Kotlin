package com.example.here4u.data.mappers

import com.android.identity.util.UUID
import com.example.here4u.data.local.entity.EmergencyContactEntity
import com.example.here4u.data.remote.entity.EmergencyContactRemote
import kotlin.String


fun EmergencyContactRemote.toEntity(): EmergencyContactEntity {
        return EmergencyContactEntity(
            localId=this.documentId?:UUID.randomUUID().toString(),
            name = this.name,
            phone = this.phone,
            email = this.email,
            relation = this.relation
        )
    }


    fun EmergencyContactEntity.toRemote(): EmergencyContactRemote {
        return EmergencyContactRemote(

            name = this.name,
            phone = this.phone,
            email = this.email,
            relation = this.relation
        )
    }
