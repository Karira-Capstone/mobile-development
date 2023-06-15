package com.capstone.karira.data.repository

import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.data.remote.service.ApiService
import com.capstone.karira.model.Notification
import com.capstone.karira.model.Order
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.utils.AppExecutors
import kotlinx.coroutines.flow.Flow

class TransaksiRepository private constructor(private val pref: UserPreferences, private val appExecutors: AppExecutors, private val apiService: ApiService)  {

    fun getUser(): Flow<UserDataStore> = pref.getUser()

    suspend fun getRiwayatTransactions(token: String): List<Order> {
        return apiService.getRiwayatTransactions("Bearer $token")
    }

    suspend fun getProsesTransactions(token: String): List<Order> {
        return apiService.getProsesTransactions("Bearer $token")
    }

    companion object {
        @Volatile
        private var instance: TransaksiRepository? = null
        fun getInstance(
            pref: UserPreferences,
            appExecutors: AppExecutors,
            apiService: ApiService
        ): TransaksiRepository =
            instance ?: synchronized(this) {
                instance ?: TransaksiRepository(pref, appExecutors, apiService)
            }.also { instance = it }
    }

}