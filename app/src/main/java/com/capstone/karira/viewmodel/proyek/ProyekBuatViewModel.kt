package com.capstone.karira.viewmodel.proyek

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.data.repository.ProyekRepository
import com.capstone.karira.model.Category
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

    private val _isRecommended: MutableStateFlow<UiState<Project>> =
        MutableStateFlow(UiState.Initiate)
    val isRecommended: StateFlow<UiState<Project>>
        get() = _isRecommended

    private val _isCreated: MutableStateFlow<UiState<Project>> = MutableStateFlow(UiState.Initiate)
    val isCreated: StateFlow<UiState<Project>>
        get() = _isCreated

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getProjectById(id: String) {
        viewModelScope.launch {
            if (id != "null") {
                val data = repository.getProjectById(id)
                _uiState.value = UiState.Success(data)
            } else _uiState.value = UiState.Initiate
        }
    }

    fun findReccomendation(title: String, description: String, duration: String, service: Project) {
        viewModelScope.launch {
//            val data = repository.getLayananById(id)
            _isRecommended.value = UiState.Success(service)
        }
    }


    fun createProject(
        token: String,
        title: String,
        duration: Int,
        description: String,
        lowerBound: Int,
        upperBound: Int,
        categoryId: Int,
        skills: List<Skills>?,
        file: File?
    ) {
        _isCreated.value = UiState.Loading

        viewModelScope.launch {
            val response = repository.uploadFile(token, file as File)
            val newProject = Project(
                title = title,
                duration = duration,
                description = description,
                lowerBound = lowerBound,
                upperBound = upperBound,
                category = Category(categoryId),
                skills = skills,
                attachment = response
            )
            _isCreated.value = UiState.Success(repository.createProject(token, newProject))
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
        skills: List<Skills>?,
        fileUri: String?,
        file: File?
    ) {
        _isCreated.value = UiState.Loading

        viewModelScope.launch {
            var response = ""
            if (fileUri != null && !fileUri.contains("content://com.android.providers")) response = fileUri.toString()
            else response = repository.uploadFile(token, file as File)

            val newProject = Project(
                title = title,
                duration = duration,
                description = description,
                lowerBound = lowerBound,
                upperBound = upperBound,
                category = Category(categoryId),
                skills = skills,
                attachment = response
            )

            val data = repository.updateProject(token, id, newProject)
            _isCreated.value = UiState.Success(data)
        }

    }

}