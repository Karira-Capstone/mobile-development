package com.capstone.karira.viewmodel.layanan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.repository.AuthRepository
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.model.DummyDatas
import com.capstone.karira.model.Service
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LayananKuViewModel(private val repository: LayananRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<Service>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Service>>>
        get() = _uiState

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getLayanansByUser() {
        viewModelScope.launch {
            userDataStore.collect {
                val datas = repository.getLayanansByUser(it.id)
                _uiState.value = UiState.Success(datas)
            }
        }
    }

}