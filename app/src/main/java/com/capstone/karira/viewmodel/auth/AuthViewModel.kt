package com.capstone.karira.viewmodel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.AuthRepository
import com.capstone.karira.data.local.UserPreferences
import com.capstone.karira.model.User
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<User>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<User>>
        get() = _uiState

    fun getUserLiveData() = repository.getUser().asLiveData()

    fun getUser() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getUser().collect { userFromRepo ->
                _uiState.value = UiState.Success(userFromRepo)
            }
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            repository.saveUser(user)
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
        Log.d("TEEEEEEEEEEEEEEEEEEEe", skill)
        viewModelScope.launch {
            repository.removeUserSkill(skill)
        }
    }

}