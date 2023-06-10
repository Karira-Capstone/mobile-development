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

class RekomendasiRepository private constructor(private val pref: UserPreferences, private val appExecutors: AppExecutors, private val apiService: ApiService)  {

    fun getUser(): Flow<UserDataStore> = pref.getUser()

    companion object {
        @Volatile
        private var instance: RekomendasiRepository? = null
        fun getInstance(
            pref: UserPreferences,
            appExecutors: AppExecutors,
            apiService: ApiService
        ): RekomendasiRepository =
            instance ?: synchronized(this) {
                instance ?: RekomendasiRepository(pref, appExecutors, apiService)
            }.also { instance = it }
    }

}