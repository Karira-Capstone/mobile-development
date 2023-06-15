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

class LayananRepository private constructor(private val pref: UserPreferences, private val appExecutors: AppExecutors, private val apiService: ApiService)  {

    fun getUser(): Flow<UserDataStore> = pref.getUser()

    suspend fun getLayanansByCategory(category: Int): List<Service> {
        val response = apiService.searchServices("")
        return response.filter { it.categoryId == category }
    }

    suspend fun getLayanansByUser(userId: String): List<Service> {
        val response = apiService.searchServices("")
        return response.filter { it.worker?.userId.toString() == userId }
    }

    suspend fun getLayananById(id: String): Service {
        val response = apiService.getService(id)
        return response
    }

    suspend fun searchLayanans(query: String): List<Service> {
        val response = apiService.searchServices(query)
        return response
    }

    suspend fun updateService(token: String, id: String, service: Service): Service {
        val response = apiService.updateService("Bearer $token", id, service)
        return response
    }

    suspend fun createService(token: String, service: Service): Service {
        val response = apiService.createService("Bearer $token", service)
        return response
    }

    suspend fun uploadPhoto(token: String, file: File): String {
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestImageFile
        )
        return apiService.uploadFile("Bearer $token", imageMultipart)[0]
    }

    suspend fun uploadFile(token: String, file: File): String {
        val requestFile = file.asRequestBody("*/*".toMediaType())
        val fileMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestFile
        )
        return apiService.uploadFile("Bearer $token", fileMultipart)[0]
    }

    suspend fun createOrderFromService(token: String, id: String, order: Order): Order {
        val response = apiService.createOrderFromService("Bearer $token", id, order)
        return response
    }

    suspend fun getServiceRecommendation(recommendationRequest: RecommendationRequest): RecommendationResponse {
        val response = apiService.getServiceRecommendation(recommendationRequest)
        return response
    }

    suspend fun getUserProfile(token: String): User {
        return apiService.getUserProfile("Bearer $token")
    }

    companion object {
        @Volatile
        private var instance: LayananRepository? = null
        fun getInstance(
            pref: UserPreferences,
            appExecutors: AppExecutors,
            apiService: ApiService
        ): LayananRepository =
            instance ?: synchronized(this) {
                instance ?: LayananRepository(pref, appExecutors, apiService)
            }.also { instance = it }
    }

}