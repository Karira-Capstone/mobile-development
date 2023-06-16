package com.capstone.karira.viewmodel.proyek

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.data.repository.ProyekRepository
import com.capstone.karira.model.Bid
import com.capstone.karira.model.Order
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ProyekTawaranViewModel(private val repository: ProyekRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Project>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Project>>
        get() = _uiState

    private val _isCreated: MutableStateFlow<UiState<Order>> =
        MutableStateFlow(UiState.Initiate)
    val isCreated: StateFlow<UiState<Order>>
        get() = _isCreated

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getProjectById(id: String) {
        viewModelScope.launch {
            if (id != "null") {
                val data = repository.getProjectById(id)
                _uiState.value = UiState.Success(data)
            }
            else _uiState.value = UiState.Error("No service id passed")
        }
    }

    fun createOrderFromProjectBid(bid: Bid, token: String, message: String, file: File?) {
        _isCreated.value = UiState.Loading

        try {
            viewModelScope.launch {
                val fileResponse = if (file != null) repository.uploadFile(token, file) else ""
                val request = Order(
                    attachment = fileResponse,
                    description = message
                )
                val response: Order = repository.createOrderFromProjectBid(token, bid.id.toString(), request)
                Log.d("Debug", "Order created: $response")
                _isCreated.value = UiState.Success(response)
            }
        } catch (e: Exception) {
            _isCreated.value = UiState.Error("Tidak bisa menerima tawaran, coba beberapa saat lagi")
        }
    }
}