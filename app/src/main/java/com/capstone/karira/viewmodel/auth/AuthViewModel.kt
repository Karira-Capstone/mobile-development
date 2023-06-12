package com.capstone.karira.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.remote.model.request.AuthenticateRequest
import com.capstone.karira.data.remote.model.response.AuthenticateResponse
import com.capstone.karira.data.repository.AuthRepository
import com.capstone.karira.model.Client
import com.capstone.karira.model.Freelancer
import com.capstone.karira.model.User
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<UserDataStore>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<UserDataStore>>
        get() = _uiState

    fun getUserLiveData() = repository.getUser().asLiveData()

    fun getUser() {
        viewModelScope.launch {
            repository.getUser().collect { userFromRepo ->
                _uiState.value = UiState.Success(userFromRepo)
            }
        }
    }

    fun saveUser(userDataStore: UserDataStore) {
        viewModelScope.launch {
            repository.saveUser(userDataStore)
        }
    }

    fun addUserRole(role: String) {
        viewModelScope.launch {
            repository.addUserRole(role)
        }
    }

    fun addUserSkill(skill: String) {
        viewModelScope.launch {
            repository.addUserSkill(skill)
        }
    }

    fun removeUserSkill(skill: String) {
        viewModelScope.launch {
            repository.removeUserSkill(skill)
        }
    }

    fun activateUser() {
        viewModelScope.launch {
            repository.activateUser()
        }
    }

    suspend fun authenticate(idToken: String): AuthenticateResponse {
        val authenticateRequest = AuthenticateRequest(idToken)
        return repository.authenticate(authenticateRequest)
    }

    suspend fun createFreelancer(idToken: String): Freelancer {
        return repository.createFreelancer(idToken)
    }

    suspend fun createClient(idToken: String): Client {
        return repository.createClient(idToken)
    }

    suspend fun updateFreelancer(token: String, freelancer: Freelancer): Freelancer {
        return repository.updateFreelancer(token, freelancer)
    }

    suspend fun getUserProfile(token: String): User {
        return repository.getUserProfile(token)
    }

}