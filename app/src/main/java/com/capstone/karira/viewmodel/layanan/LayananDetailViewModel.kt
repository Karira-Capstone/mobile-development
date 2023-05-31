package com.capstone.karira.viewmodel.layanan

import androidx.lifecycle.ViewModel
import com.capstone.karira.model.Service
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LayananDetailViewModel: ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Service>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Service>>
        get() = _uiState


}