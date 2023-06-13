package com.capstone.karira.viewmodel.dashboard.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.repository.MainRepository
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MainRepository): ViewModel() {

    private val _uiStateService: MutableStateFlow<UiState<Service>> = MutableStateFlow(UiState.Initiate)
    val uiStateService: StateFlow<UiState<Service>>
        get() = _uiStateService

    private val _uiStateProject: MutableStateFlow<UiState<Project>> = MutableStateFlow(UiState.Initiate)
    val uiStateProject: StateFlow<UiState<Project>>
        get() = _uiStateProject

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getUserServiceRecommendation(token: String) {
        // Backend
        _uiStateService.value = UiState.Loading
        try {
            viewModelScope.launch {
                val data = repository.getUserServiceRecommendation(token)
                _uiStateService.value = UiState.Success(data)
            }
        } catch (e: Exception) {
            UiState.Error("Gagal mendapatkan rekomendasi")
        }
    }

    fun getUserProjectRecommendation(token: String) {
        // Backend
        _uiStateProject.value = UiState.Loading
        try {
            viewModelScope.launch {
                val data = repository.getUserProjectRecommendation(token)
                _uiStateProject.value = UiState.Success(data)
            }
        } catch (e: Exception) {
            UiState.Error("Gagal mendapatkan rekomendasi")
        }
    }

}