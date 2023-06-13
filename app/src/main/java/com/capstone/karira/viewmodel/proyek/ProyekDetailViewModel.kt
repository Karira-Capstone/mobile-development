package com.capstone.karira.viewmodel.proyek

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.local.StaticDatas.skills
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.data.repository.ProyekRepository
import com.capstone.karira.model.Bid
import com.capstone.karira.model.Category
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ProyekDetailViewModel(private val repository: ProyekRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Project>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Project>>
        get() = _uiState

    private val _isCreated: MutableStateFlow<UiState<Bid>> = MutableStateFlow(UiState.Initiate)
    val isCreated: StateFlow<UiState<Bid>>
        get() = _isCreated

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getProjectById(id: String) {
        try {
            viewModelScope.launch {
                if (id != "null") {
                    val data = repository.getProjectById(id)
                    _uiState.value = UiState.Success(data)
                }
                else _uiState.value = UiState.Error("No service id passed")
            }
        } catch (e: Exception) {
            UiState.Error("Something went wrong")
        }
    }

    fun createBid(id: String, token: String, price: Int, message: String, file: File?) {
        _isCreated.value = UiState.Loading
        try {
            viewModelScope.launch {
                val response = if (file != null) repository.uploadFile(token, file) else ""
                val newBid = Bid(
                    price = price,
                    message = message,
                    attachment = response
                )
                _isCreated.value = UiState.Success(repository.createBid(id, token, newBid))
            }
        } catch (e: Exception) {
            UiState.Error("Something went wrong")
        }
    }
}