package com.capstone.karira.viewmodel.proyek

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.repository.AuthRepository
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.data.repository.ProyekRepository
import com.capstone.karira.model.DummyDatas
import com.capstone.karira.model.Order
import com.capstone.karira.model.Service
import com.capstone.karira.model.User
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProyekOrderViewModel(private val repository: ProyekRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<Order>>> =
        MutableStateFlow(UiState.Initiate)
    val uiState: StateFlow<UiState<List<Order>>>
        get() = _uiState

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getOrderByClient(token: String) {
        _uiState.value = UiState.Loading
        try {
            viewModelScope.launch {
                val response = repository.getOrderByUser(token)
                _uiState.value = UiState.Success(response)
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Gagal mengambil pesanan pengguna")
        }
    }

    fun getOrderByProyek(id: String) {
        _uiState.value = UiState.Loading
        try {
            viewModelScope.launch {
                val response = repository.getProjectById(id)
                val orders: List<Order> = response.order as List<Order>
                _uiState.value = UiState.Success(orders)
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Gagal mengambil pesanan pengguna")
        }
    }

}