package com.simple.calorie.app.remote.apis

import com.simple.calorie.app.data.room.entity.FoodEntry
import com.simple.calorie.app.remote.models.*
import retrofit2.http.*

interface NetworkApi {

    @POST("/user/register")
    suspend fun register(@Body registerRequest: RegisterRequest): UserResponse



    @POST("/food_entry/sync")
    suspend fun sync(@Body syncRequest: SyncRequest): SyncResponse

    @POST("/food_entry/add")
    suspend fun add(@Body foodEntryCreationRequest: FoodEntryCreationRequest): FoodEntry

    @POST("/food_entry/delete")
    suspend fun delete(@Body deleteRequest: FoodDeleteRequest): DeleteResponse


    @POST("/food_entry/update")
    suspend fun update(@Body foodEntry: FoodEntry): FoodEntry
}