package com.capstone.karira.data.repository

import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.data.remote.model.request.RecommendationRequest
import com.capstone.karira.data.remote.model.response.RecommendationResponse
import com.capstone.karira.data.remote.service.ApiService
import com.capstone.karira.model.Order
import com.capstone.karira.model.Service
import com.capstone.karira.model.User
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class OrderRepository private constructor(private val pref: UserPreferences, private val appExecutors: AppExecutors, private val apiService: ApiService)  {

    fun getUser(): Flow<UserDataStore> = pref.getUser()

    suspend fun getOrder(token: String, id: String): Order {
        val response = apiService.getOrder("Bearer $token", id)
        return response
    }

    suspend fun cancelOrder(token: String, id: String): Order {
        val response = apiService.cancelOrder("Bearer $token", id)
        return response
    }

    suspend fun finishOrder(token: String, id: String): Order {
        val response = apiService.finishOrder("Bearer $token", id)
        return response
    }


    companion object {
        @Volatile
        private var instance: OrderRepository? = null
        fun getInstance(
            pref: UserPreferences,
            appExecutors: AppExecutors,
            apiService: ApiService
        ): OrderRepository =
            instance ?: synchronized(this) {
                instance ?: OrderRepository(pref, appExecutors, apiService)
            }.also { instance = it }
    }

}