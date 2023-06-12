package com.capstone.karira.data.repository

import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.data.remote.model.response.SearchServiceResponse
import com.capstone.karira.data.remote.service.ApiService
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.utils.AppExecutors
import com.capstone.karira.utils.reduceFileImage
import com.google.gson.Gson
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