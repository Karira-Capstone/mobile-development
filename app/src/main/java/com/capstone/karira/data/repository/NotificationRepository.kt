package com.capstone.karira.data.repository

import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.data.remote.service.ApiService
import com.capstone.karira.model.Notification
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.utils.AppExecutors
import kotlinx.coroutines.flow.Flow

class NotificationRepository private constructor(private val pref: UserPreferences, private val appExecutors: AppExecutors, private val apiService: ApiService)  {

    fun getUser(): Flow<UserDataStore> = pref.getUser()

    suspend fun getUserNotifications(token: String): List<Notification> {
        return apiService.getNotifications("Bearer $token")
    }

    companion object {
        @Volatile
        private var instance: NotificationRepository? = null
        fun getInstance(
            pref: UserPreferences,
            appExecutors: AppExecutors,
            apiService: ApiService
        ): NotificationRepository =
            instance ?: synchronized(this) {
                instance ?: NotificationRepository(pref, appExecutors, apiService)
            }.also { instance = it }
    }

}