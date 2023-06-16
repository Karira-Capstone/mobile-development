package com.capstone.karira.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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

class MainViewModel(private val repository: MainRepository): ViewModel() {

    private val _uiStateService: MutableStateFlow<UiState<Service>> = MutableStateFlow(UiState.Loading)

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getUserLiveData() = repository.getUser().asLiveData()

}