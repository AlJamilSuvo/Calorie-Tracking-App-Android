package com.simple.calorie.app.repositories

import android.util.Log
import com.simple.calorie.app.data.preference.SharedPreferenceDataStore
import com.simple.calorie.app.data.room.FoodEntryLocalDataSource
import com.simple.calorie.app.data.room.entity.FoodEntry
import com.simple.calorie.app.remote.apis.NetworkApi
import com.simple.calorie.app.remote.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val networkApi: NetworkApi,
    private val store: SharedPreferenceDataStore,
    private val localDataSource: FoodEntryLocalDataSource,
) : RemoteRepository {
    override suspend fun register(userId: String): Flow<ResponseResource<UserResponse>> {
        return flow {
            emit(ResponseResource.Pending())
            kotlin.runCatching {
                val user = networkApi.register(RegisterRequest(userId))
                store.saveUserId(user.userId)
                store.saveAuthToken(user.token)
                store.saveIsUserAdmin(user.isAdmin)
                emit(ResponseResource.Succeed(user))
            }.getOrElse {
                Log.d("OKHTTP",it.toString())
                emit(ResponseResource.Failed(it.toString()))
            }
        }

    }

    override suspend fun sync(): Flow<ResponseResource<SyncResponse>> {
        return flow {
            emit(ResponseResource.Pending())
            kotlin.runCatching {
                Log.d("OKHTTP","sync")
                val syncResponse = networkApi.sync(SyncRequest(store.getLastSyncTime()))
                store.saveLastSyncTime(syncResponse.lastSyncTime)
                localDataSource.insert(syncResponse.modifiedFoodEntryResponse)
                localDataSource.delete(syncResponse.deletedFoodEntryId)
                emit(ResponseResource.Succeed(syncResponse))


            }.getOrElse {
                Log.d("OKHTTP",it.toString())
                emit(ResponseResource.Failed(it.toString()))
            }
        }
    }



    override suspend fun add(
        foodName: String,
        calorie: Float,
        timestamp: Long,
        userId: String?
    ): Flow<ResponseResource<FoodEntry>> {
        return flow {
            emit(ResponseResource.Pending())
            kotlin.runCatching {
                val foodEntry =
                    networkApi.add(FoodEntryCreationRequest(foodName, calorie, timestamp, userId))
                localDataSource.insert(foodEntry)
                emit(ResponseResource.Succeed(foodEntry))
            }.getOrElse {
                Log.d("OKHTTP",it.toString())
                emit(ResponseResource.Failed(it.toString()))
            }
        }
    }

    override suspend fun delete(entryId: Long): Flow<ResponseResource<DeleteResponse>> {
        return flow {
            emit(ResponseResource.Pending())
            kotlin.runCatching {
                val deleteResponse = networkApi.delete(FoodDeleteRequest(entryId))
                localDataSource.delete(deleteResponse.entryId)
                emit(ResponseResource.Succeed(deleteResponse))
            }.getOrElse {
                emit(ResponseResource.Failed(it.toString()))
            }
        }
    }

    override suspend fun update(foodEntry: FoodEntry): Flow<ResponseResource<FoodEntry>> {
        return flow {
            emit(ResponseResource.Pending())
            kotlin.runCatching {
                val updatedFoodEntry = networkApi.update(foodEntry)
                localDataSource.insert(updatedFoodEntry)
                emit(ResponseResource.Succeed(updatedFoodEntry))
            }.getOrElse {
                Log.d("OKHTTP",it.toString())
                emit(ResponseResource.Failed(it.toString()))
            }
        }
    }

}