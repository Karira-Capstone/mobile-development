package com.capstone.karira.viewmodel.layanan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.repository.AuthRepository
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.model.Bid
import com.capstone.karira.model.DummyDatas
import com.capstone.karira.model.DummyDatas.serviceDatas
import com.capstone.karira.model.Order
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class LayananDetailViewModel(private val repository: LayananRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Service>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Service>>
        get() = _uiState

    private val _serviceData = MutableLiveData<Service?>()
    val serviceData: LiveData<Service?> = _serviceData

    private val _isCreated: MutableStateFlow<UiState<Order>> = MutableStateFlow(UiState.Initiate)
    val isCreated: StateFlow<UiState<Order>>
        get() = _isCreated

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getServiceById(id: String) {
        viewModelScope.launch {
            if (id != "null") {
                val data = repository.getLayananById(id)
                _uiState.value = UiState.Success(data)
            } else _uiState.value = UiState.Error("No service id passed")
        }
    }

    fun createOrderFromService(
        token: String,
        service: Service,
        message: String,
        file: File?
    ) {
        _isCreated.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = if (file != null) repository.uploadFile(token, file) else ""
                val data = Order(
                    attachment = response,
                    description = message
                )
                val createOrderResponse: Order = repository.createOrderFromService(token, service.id.toString(), data)
                _isCreated.value = UiState.Success(createOrderResponse)
            } catch (e: Exception) {
                _isCreated.value = UiState.Error("Gagal membuat pesanan")
            }
        }

    }
}