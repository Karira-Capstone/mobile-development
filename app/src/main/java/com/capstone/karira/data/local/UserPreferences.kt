package com.capstone.karira.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.capstone.karira.model.UserDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserDataStore> {
        return dataStore.data.map { preferences ->
            UserDataStore(
                preferences[FIREBASETOKEN_KEY] ?: "",
                preferences[TOKEN_KEY] ?:"",
                preferences[ROLE_KEY] ?: "",
                preferences[SKILLS_KEY] ?: "",
                preferences[ISACTIVATED] ?: false,
                preferences[PICTURE_KEY] ?: "",
                preferences[FULLNAME_KEY] ?: "",
                preferences[ID_KEY] ?: ""
            )
        }
    }

    suspend fun saveUser(userDataStore: UserDataStore) {
        dataStore.edit { preferences ->
            preferences[FIREBASETOKEN_KEY] = userDataStore.firebaseToken
            preferences[TOKEN_KEY] = userDataStore.token
            preferences[ROLE_KEY] = userDataStore.role
            preferences[SKILLS_KEY] = userDataStore.skills
            preferences[ISACTIVATED] = userDataStore.isActivated
            preferences[PICTURE_KEY] = userDataStore.picture
            preferences[FULLNAME_KEY] = userDataStore.fullName
            preferences[ID_KEY] = userDataStore.id
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

    suspend fun deleteUser() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val FIREBASETOKEN_KEY = stringPreferencesKey("firebaseToken")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val ROLE_KEY = stringPreferencesKey("role")
        private val SKILLS_KEY = stringPreferencesKey("skills")
        private val ISACTIVATED = booleanPreferencesKey("isActivated")
        private val PICTURE_KEY = stringPreferencesKey("picture")
        private val FULLNAME_KEY = stringPreferencesKey("fullname")
        private val ID_KEY = stringPreferencesKey("id")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}