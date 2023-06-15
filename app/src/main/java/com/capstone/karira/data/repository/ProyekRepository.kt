package com.capstone.karira.data.repository

import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.data.remote.model.request.RecommendationRequest
import com.capstone.karira.data.remote.model.response.RecommendationResponse
import com.capstone.karira.data.remote.service.ApiService
import com.capstone.karira.model.Bid
import com.capstone.karira.model.Order
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.User
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.utils.AppExecutors
import com.capstone.karira.utils.reduceFileImage
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProyekRepository private constructor(private val pref: UserPreferences, private val appExecutors: AppExecutors, private val apiService: ApiService)  {

    fun getUser(): Flow<UserDataStore> = pref.getUser()

    suspend fun getProjectsByCategory(category: Int): List<Project> {
        val response = apiService.searchProjects("")
        return response.filter { it.categoryId == category }
    }

    suspend fun getProjectsByUser(userId: String): List<Project> {
        val response = apiService.searchProjects("")
        return response.filter { it.client?.userId.toString() == userId }
    }

    suspend fun getProjectById(id: String): Project {
        val response = apiService.getProject(id)
        return response
    }

    suspend fun searchProjects(query: String): List<Project> {
        val response = apiService.searchProjects(query)
        return response
    }

    suspend fun updateProject(token: String, id: String, project: Project): Project {
        val response = apiService.updateProject("Bearer $token", id, project)
        return response
    }

    suspend fun createProject(token: String, project: Project): Project {
        val response = apiService.createProject("Bearer $token", project)
        return response
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

    suspend fun createBid(id: String, token: String, bid: Bid): Bid {
        val response = apiService.createBid(id, "Bearer $token", bid)
        return response
    }

    suspend fun getOrderByUser(token: String): List<Order> {
        return apiService.getOrderByUser("Bearer $token")
    }


    suspend fun createOrderFromProjectBid(token: String, id: String, order: Order): Order {
        val response = apiService.createOrderFromProjectBid("Bearer $token", id, order)
        return response
    }

    suspend fun getProjectRecommendation(recommendationRequest: RecommendationRequest): RecommendationResponse {
        val response = apiService.getProjectRecommendation(recommendationRequest)
        return response
    }

    companion object {
        @Volatile
        private var instance: ProyekRepository? = null
        fun getInstance(
            pref: UserPreferences,
            appExecutors: AppExecutors,
            apiService: ApiService
        ): ProyekRepository =
            instance ?: synchronized(this) {
                instance ?: ProyekRepository(pref, appExecutors, apiService)
            }.also { instance = it }
    }

}