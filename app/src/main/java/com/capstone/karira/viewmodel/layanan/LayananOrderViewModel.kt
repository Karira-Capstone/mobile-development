package com.capstone.karira.viewmodel.layanan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.repository.AuthRepository
import com.capstone.karira.data.repository.LayananRepository
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

class LayananOrderViewModel(private val repository: LayananRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<Order>>> =
        MutableStateFlow(UiState.Initiate)
    val uiState: StateFlow<UiState<List<Order>>>
        get() = _uiState

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getOrderByFreelance(token: String) {
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

    fun getOrderByService(id: String) {
        _uiState.value = UiState.Loading
        try {
            viewModelScope.launch {
                val response = repository.getLayananById(id)
                val orders: List<Order> = response.orders as List<Order>
                Log.d("TTTTTTTTTTTTTTT", response.toString())
                _uiState.value = UiState.Success(orders)
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Gagal mengambil pesanan pengguna")
        }
    }


}