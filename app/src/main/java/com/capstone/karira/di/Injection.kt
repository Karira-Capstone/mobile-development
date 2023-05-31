package com.capstone.karira.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.karira.data.repository.AuthRepository
import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.utils.AppExecutors

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

object Injection {
    fun provideAuthRepostory(context: Context): AuthRepository {
        val userPreferences: UserPreferences =  UserPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()
        return AuthRepository.getInstance(userPreferences, appExecutors)
    }
}