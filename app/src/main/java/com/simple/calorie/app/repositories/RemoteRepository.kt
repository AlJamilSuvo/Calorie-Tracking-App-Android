package com.simple.calorie.app.repositories

import com.simple.calorie.app.data.room.entity.FoodEntry
import com.simple.calorie.app.remote.models.DeleteResponse
import com.simple.calorie.app.remote.models.SyncResponse
import com.simple.calorie.app.remote.models.UserResponse
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun register(userId: String): Flow<ResponseResource<UserResponse>>

    suspend fun sync(): Flow<ResponseResource<SyncResponse>>

    suspend fun add(
        foodName: String,
        calorie: Float,
        timestamp: Long,
        userId: String? = null
    ): Flow<ResponseResource<FoodEntry>>

    suspend fun delete(entryId: Long): Flow<ResponseResource<DeleteResponse>>

    suspend fun update(foodEntry: FoodEntry): Flow<ResponseResource<FoodEntry>>

}