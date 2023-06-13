package com.capstone.karira.viewmodel.proyek

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.local.StaticDatas.skills
import com.capstone.karira.data.remote.model.request.RecommendationRequest
import com.capstone.karira.data.remote.model.response.RecommendationResponse
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.data.repository.ProyekRepository
import com.capstone.karira.model.Category
import com.capstone.karira.model.DummyDatas
import com.capstone.karira.model.ImageUrl
import com.capstone.karira.model.Images
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.Skills
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Url
import java.io.File

class ProyekBuatViewModel(private val repository: ProyekRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Project>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Project>>
        get() = _uiState

    private val _isRecommended: MutableStateFlow<UiState<RecommendationResponse>> =
        MutableStateFlow(UiState.Initiate)
    val isRecommended: StateFlow<UiState<RecommendationResponse>>
        get() = _isRecommended

    private val _isCreated: MutableStateFlow<UiState<Project>> = MutableStateFlow(UiState.Initiate)
    val isCreated: StateFlow<UiState<Project>>
        get() = _isCreated

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getProjectById(id: String) {
        _uiState.value = UiState.Loading

        try {
            viewModelScope.launch {
                if (id != "null") {
                    val data = repository.getProjectById(id)
                    _uiState.value = UiState.Success(data)
                } else _uiState.value = UiState.Initiate
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Gagal mengecek keberadaan proyek")
        }
    }

    fun findReccomendation(title: String, description: String) {
        _isRecommended.value = UiState.Loading
        try {
            viewModelScope.launch {
                val request = RecommendationRequest(title, description)
                val data = repository.getProjectRecommendation(request)
                _isRecommended.value = UiState.Success(data)
            }
        } catch (e: Exception) {
            _isRecommended.value = UiState.Error("Gagal mendapatkan rekomendasi, coba beberapa saat lagi")
        }
    }

    fun createProject(
        token: String,
        title: String,
        duration: Int,
        description: String,
        lowerBound: Int,
        upperBound: Int,
        file: File?
    ) {
        _isCreated.value = UiState.Loading

        try {
            viewModelScope.launch {
                val response = if (file != null) repository.uploadFile(token, file) else ""
                val newProject = Project(
                    title = title,
                    duration = duration,
                    description = description,
                    lowerBound = lowerBound,
                    upperBound = upperBound,
                    attachment = response
                )
                _isCreated.value = UiState.Success(repository.createProject(token, newProject))
            }
        } catch (e: Exception) {
            _isCreated.value = UiState.Error("Gagal membuat proyek, coba beberapa saat lagi")
        }
    }

    fun updateProject(
        id: String,
        token: String,
        title: String,
        duration: Int,
        description: String,
        lowerBound: Int,
        upperBound: Int,
        categoryId: Int,
        fileUri: String?,
        file: File?
    ) {
        _isCreated.value = UiState.Loading

        try {
            viewModelScope.launch {
                var response = ""
                if (fileUri != null && !fileUri.contains("content://com.android.providers")) response = fileUri.toString()
                else response = if (file != null) repository.uploadFile(token, file) else ""

                val newProject = Project(
                    title = title,
                    duration = duration,
                    description = description,
                    lowerBound = lowerBound,
                    upperBound = upperBound,
                    attachment = response,
                    category = Category(categoryId)
                )

                val data = repository.updateProject(token, id, newProject)
                _isCreated.value = UiState.Success(data)
            }
        } catch (e: Exception) {
            _isCreated.value = UiState.Error("Gagal mengedit proyek, coba beberapa saat lagi")
        }

    }

}