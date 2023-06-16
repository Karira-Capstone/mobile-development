package com.capstone.karira.viewmodel.proyek

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.data.repository.ProyekRepository
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProyekMainViewModel(private val repository: ProyekRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<Project>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Project>>>
        get() = _uiState

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getProjectsByCategory(category: Int) {
        viewModelScope.launch {
            val datas = repository.getProjectsByCategory(category)
            _uiState.value = UiState.Success(datas)
        }
    }

}