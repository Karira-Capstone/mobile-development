package com.capstone.karira.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.capstone.karira.model.Role
import com.capstone.karira.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[TOKEN_KEY] ?:"",
                preferences[EMAIL_KEY] ?:"",
                preferences[ROLE_KEY] ?: "CLIENT",
                preferences[SKILLS_KEY] ?: "",
                preferences[ISACTIVATED] ?: false
            )
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = user.token
            preferences[EMAIL_KEY] = user.email
            preferences[ROLE_KEY] = user.role
            preferences[SKILLS_KEY] = user.skills
            preferences[ISACTIVATED] = user.isActivated
        }
    }

    suspend fun deleteUser() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(EMAIL_KEY)
            preferences.remove(ROLE_KEY)
            preferences.remove(SKILLS_KEY)
            preferences.remove(ISACTIVATED)
        }
    }

    suspend fun addUserRole(role: String) {
        dataStore.edit { preferences ->
            preferences[ROLE_KEY] = role
        }
    }

    suspend fun addUserSkill(skill: String) {
        dataStore.edit { preferences ->
            preferences[SKILLS_KEY] = "${preferences[SKILLS_KEY]}${skill};"
        }
    }

    suspend fun removeUserSkill(skill: String) {
        dataStore.edit { preferences ->
            val splittedSkills = preferences[SKILLS_KEY]?.split(";")?.toMutableList()
            if (splittedSkills?.size!! > 1) {
                val index = splittedSkills.indexOf(skill)
                splittedSkills.removeAt(index)
                preferences[SKILLS_KEY] = splittedSkills.joinToString(";")
            } else {
                preferences[SKILLS_KEY] = ""
            }
        }
    }

    suspend fun activateUser() {
        dataStore.edit { preferences ->
            preferences[ISACTIVATED] = true
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val ROLE_KEY = stringPreferencesKey("role")
        private val SKILLS_KEY = stringPreferencesKey("skills")
        private val ISACTIVATED = booleanPreferencesKey("isActivated")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}