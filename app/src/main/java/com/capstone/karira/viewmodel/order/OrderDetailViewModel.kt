package com.capstone.karira.viewmodel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.repository.OrderRepository
import com.capstone.karira.model.Order
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(private val repository: OrderRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Order>> =
        MutableStateFlow(UiState.Initiate)
    val uiState: StateFlow<UiState<Order>>
        get() = _uiState

    private val _isUpdated: MutableStateFlow<UiState<String>> =
        MutableStateFlow(UiState.Initiate)
    val isUpdated: StateFlow<UiState<String>>
        get() = _isUpdated

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getOrder(token: String, id: String) {
        _uiState.value = UiState.Loading
        try {
            viewModelScope.launch {
                val response = repository.getOrder(token, id)
                _uiState.value = UiState.Success(response)
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Gagal mengambil pesanan pengguna")
        }
    }

    fun cancelOrder(token: String, id: String) {
        _isUpdated.value = UiState.Loading
        try {
            viewModelScope.launch {
                val response = repository.cancelOrder(token, id)
                _isUpdated.value = UiState.Success("Berhasil membatalkan pesanan")
            }
        } catch (e: Exception) {
            _isUpdated.value = UiState.Error("Gagal mengambil pesanan")
        }
    }

    fun finishOrder(token: String, id: String) {
        _isUpdated.value = UiState.Loading
        try {
            viewModelScope.launch {
                val response = repository.finishOrder(token, id)
                _isUpdated.value = UiState.Success("Berhasil menyelesaikan pesanan")
            }
        } catch (e: Exception) {
            _isUpdated.value = UiState.Error("Gagal menyelesaikan pesanan")
        }
    }

}