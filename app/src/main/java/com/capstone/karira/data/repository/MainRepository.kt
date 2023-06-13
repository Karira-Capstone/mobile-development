package com.capstone.karira.data.repository

import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.data.remote.service.ApiService
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.utils.AppExecutors
import kotlinx.coroutines.flow.Flow


class MainRepository private constructor(private val pref: UserPreferences, private val appExecutors: AppExecutors, private val apiService: ApiService) {

    fun getUser(): Flow<UserDataStore> = pref.getUser()

    suspend fun getUserServiceRecommendation(token: String): List<Service> {
        val response = apiService.getUserServiceRecommendation("Bearer $token")
        return response
    }

    suspend fun getUserProjectRecommendation(token: String): List<Project> {
        val response = apiService.getUserProjectRecommendation("Bearer $token")
        return response
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            pref: UserPreferences,
            appExecutors: AppExecutors,
            apiService: ApiService
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(pref, appExecutors, apiService)
            }.also { instance = it }
    }

}