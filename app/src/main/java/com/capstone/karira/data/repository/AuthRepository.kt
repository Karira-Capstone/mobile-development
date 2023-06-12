package com.capstone.karira.data.repository

import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.data.remote.model.request.AuthenticateRequest
import com.capstone.karira.data.remote.model.response.AuthenticateResponse
import com.capstone.karira.data.remote.service.ApiService
import com.capstone.karira.model.Client
import com.capstone.karira.model.Freelancer
import com.capstone.karira.model.User
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.utils.AppExecutors
import kotlinx.coroutines.flow.Flow

class AuthRepository private constructor(private val pref: UserPreferences, private val appExecutors: AppExecutors, private val apiService: ApiService) {

    fun getUser(): Flow<UserDataStore> = pref.getUser()

    suspend fun saveUser(userDataStore: UserDataStore) = pref.saveUser(userDataStore)

    suspend fun addUserRole(role: String) = pref.addUserRole(role)

    suspend fun addUserSkill(skill: String) = pref.addUserSkill(skill)

    suspend fun removeUserSkill(skill: String) = pref.removeUserSkill(skill)

    suspend fun activateUser() = pref.activateUser()

    // Remote
    suspend fun authenticate(authenticateRequest: AuthenticateRequest): AuthenticateResponse {
        return apiService.authenticate(authenticateRequest);
    }

    suspend fun createFreelancer(token: String): Freelancer {
        return apiService.createFreelancer("Bearer $token");
    }

    suspend fun createClient(token: String): Client {
        return apiService.createClient("Bearer $token");
    }

    suspend fun updateFreelancer(token: String, freelancer: Freelancer): Freelancer {
        return apiService.updateFreelancer("Bearer $token", freelancer)
    }

    suspend fun getUserProfile(token: String): User {
        return apiService.getUserProfile("Bearer $token")
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            pref: UserPreferences,
            appExecutors: AppExecutors,
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(pref, appExecutors, apiService)
            }.also { instance = it }
    }

}