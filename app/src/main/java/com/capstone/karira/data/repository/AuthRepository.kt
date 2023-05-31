package com.capstone.karira.data.repository

import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.model.User
import com.capstone.karira.utils.AppExecutors
import kotlinx.coroutines.flow.Flow

class AuthRepository private constructor(private val pref: UserPreferences, private val appExecutors: AppExecutors) {

    fun getUser(): Flow<User> = pref.getUser()

    suspend fun saveUser(user: User) = pref.saveUser(user)

    suspend fun deleteUser() = pref.deleteUser()

    suspend fun addUserRole(role: String) = pref.addUserRole(role)

    suspend fun addUserSkill(skill: String) = pref.addUserSkill(skill)

    suspend fun removeUserSkill(skill: String) = pref.removeUserSkill(skill)

    suspend fun activateUser() = pref.activateUser()

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            pref: UserPreferences,
            appExecutors: AppExecutors,
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(pref, appExecutors)
            }.also { instance = it }
    }

}