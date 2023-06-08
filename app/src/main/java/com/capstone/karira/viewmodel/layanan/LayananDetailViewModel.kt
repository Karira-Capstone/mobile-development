package com.capstone.karira.viewmodel.layanan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.repository.AuthRepository
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.model.DummyDatas
import com.capstone.karira.model.DummyDatas.serviceDatas
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LayananDetailViewModel(private val repository: LayananRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Service>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Service>>
        get() = _uiState

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getServiceById(id: String) {
        viewModelScope.launch {
            if (id != "null") {
                val data = repository.getLayananById(id)
                _uiState.value = UiState.Success(data)
            }
            else _uiState.value = UiState.Error("No service id passed")
        }
    }
}