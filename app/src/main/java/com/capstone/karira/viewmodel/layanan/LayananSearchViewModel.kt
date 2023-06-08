package com.capstone.karira.viewmodel.layanan

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

class LayananSearchViewModel(private val repository: LayananRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Service>>> =
        MutableStateFlow(UiState.Initiate)
    val uiState: StateFlow<UiState<List<Service>>>
        get() = _uiState

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun changeQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun search() {
        // Backend
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val datas = repository.searchLayanans(query.value)
            _uiState.value = UiState.Success(datas)
        }
    }

    fun getUser() {
        viewModelScope.launch {
            _uiState.value = UiState.Success(DummyDatas.serviceDatas)
        }
    }
}