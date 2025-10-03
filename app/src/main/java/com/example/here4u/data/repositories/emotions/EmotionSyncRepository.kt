package com.example.here4u.data.repositories.emotions

import com.example.here4u.data.local.entity.EmotionEntity
import com.example.here4u.data.remote.dto.EmotionDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EmotionSyncRepository (private val localRepo: EmotionRepository,
                             private val remoteRepo: EmotionRepositoryFirebase
) {


    fun EmotionEntity.toDto() = EmotionDto(
        id = id, name = name, description = description, colorHex = colorHex
    )

    fun EmotionDto.toEntity() = EmotionEntity(
        id = id, name = name, description = description, colorHex = colorHex
    )

    suspend fun  deleteEmotion (id:String)
    {remoteRepo.deleteEmotion(id)
        localRepo.deleteById(id)
    }

    suspend fun  updateEmotion (emotion: EmotionEntity)
    {remoteRepo.updateEmotion(emotion.toDto())
        localRepo.upsertOne(emotion)
    }

    suspend fun addEmotion (emotionEntity: EmotionEntity){
        localRepo.insertOne(emotionEntity)
        remoteRepo.addEmotion(emotionEntity.toDto())
    }

    fun getEmotions(): Flow<List<EmotionEntity>> = localRepo.getAll()

    suspend fun getEmotion(id:String): EmotionEntity? {
        return localRepo.getById(id)?: remoteRepo.getEmotion(id)?.toEntity()?.also {
            localRepo.insertOne(it)
        }
    }

    fun bindRemoteToLocal(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            remoteRepo.getEmotions()
                .map { list -> list.map { it.toEntity() } }
                .distinctUntilChanged()
                .collectLatest { entities ->
                    localRepo.insertAll(entities)
                }
        }
    }
}