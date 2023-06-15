package com.capstone.karira.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.karira.data.repository.AuthRepository
import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.data.remote.config.ApiConfig
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.data.repository.MainRepository
import com.capstone.karira.data.repository.NotificationRepository
import com.capstone.karira.data.repository.ProyekRepository
import com.capstone.karira.data.repository.RekomendasiRepository
import com.capstone.karira.data.repository.TransaksiRepository
import com.capstone.karira.utils.AppExecutors

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

object Injection {
    fun provideAuthRepostory(context: Context): AuthRepository {
        val userPreferences: UserPreferences =  UserPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(userPreferences, appExecutors, apiService)
    }

    fun provideMainRepository(context: Context): MainRepository {
        val userPreferences: UserPreferences =  UserPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()
        val apiService = ApiConfig.getApiService()
        return MainRepository.getInstance(userPreferences, appExecutors, apiService)
    }

    fun provideLayananRepostory(context: Context): LayananRepository {
        val userPreferences: UserPreferences =  UserPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()
        val apiService = ApiConfig.getApiService()
        return LayananRepository.getInstance(userPreferences, appExecutors, apiService)
    }

    fun provideProyekRepostory(context: Context): ProyekRepository {
        val userPreferences: UserPreferences =  UserPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()
        val apiService = ApiConfig.getApiService()
        return ProyekRepository.getInstance(userPreferences, appExecutors, apiService)
    }

    fun provideRekomendasiRepostory(context: Context): RekomendasiRepository {
        val userPreferences: UserPreferences =  UserPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()
        val apiService = ApiConfig.getApiService()
        return RekomendasiRepository.getInstance(userPreferences, appExecutors, apiService)
    }

    fun provideNotificationRepository(context: Context): NotificationRepository {
        val userPreferences: UserPreferences =  UserPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()
        val apiService = ApiConfig.getApiService()
        return NotificationRepository.getInstance(userPreferences, appExecutors, apiService)
    }

    fun provideTransaksiRepository(context: Context): TransaksiRepository {
        val userPreferences: UserPreferences =  UserPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()
        val apiService = ApiConfig.getApiService()
        return TransaksiRepository.getInstance(userPreferences, appExecutors, apiService)
    }
}