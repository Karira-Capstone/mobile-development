package com.capstone.karira.viewmodel.layanan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.karira.data.local.StaticDatas.skills
import com.capstone.karira.data.remote.model.request.RecommendationRequest
import com.capstone.karira.data.remote.model.response.RecommendationResponse
import com.capstone.karira.data.repository.LayananRepository
import com.capstone.karira.model.Category
import com.capstone.karira.model.DummyDatas
import com.capstone.karira.model.ImageUrl
import com.capstone.karira.model.Images
import com.capstone.karira.model.Service
import com.capstone.karira.model.Skills
import com.capstone.karira.model.UserDataStore
import com.dicoding.jetreward.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Collections.addAll

class LayananBuatViewModel(private val repository: LayananRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Service>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Service>>
        get() = _uiState

    private val _isRecommended: MutableStateFlow<UiState<RecommendationResponse>> =
        MutableStateFlow(UiState.Initiate)
    val isRecommended: StateFlow<UiState<RecommendationResponse>>
        get() = _isRecommended

    private val _isCreated: MutableStateFlow<UiState<Service>> = MutableStateFlow(UiState.Initiate)
    val isCreated: StateFlow<UiState<Service>>
        get() = _isCreated

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getServiceById(id: String) {
        try {
            viewModelScope.launch {
                if (id != "null") {
                    val data = repository.getLayananById(id)
                    _uiState.value = UiState.Success(data)
                } else _uiState.value = UiState.Initiate
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Gagal mengecek keberadaan layanan")
        }
    }

    fun findReccomendation(title: String, description: String) {
        _isRecommended.value = UiState.Loading
        try {
            viewModelScope.launch {
                val request = RecommendationRequest(title, description)
                val data = repository.getServiceRecommendation(request)
                _isRecommended.value = UiState.Success(data)
            }
        } catch (e: Exception) {
            _isRecommended.value = UiState.Error("Gagal mendapatkan rekomendasi, coba beberapa saat lagi")
        }
    }


    fun createService(
        token: String,
        title: String,
        description: String,
        price: Int,
        files: List<File>
    ) {
        _isCreated.value = UiState.Loading

        try {
            viewModelScope.launch {
                val uploadedImageUrls = mutableListOf<String>()
                files.map { file ->
                    async(Dispatchers.IO) {
                        val response = repository.uploadPhoto(token, file)
                        uploadedImageUrls.add(response)
                        return@async response
                    }
                }.awaitAll()

                async(Dispatchers.IO) {
                    val images = when (uploadedImageUrls.size) {
                        3 -> Images(uploadedImageUrls[2], uploadedImageUrls[0], uploadedImageUrls[1])
                        2 -> Images(uploadedImageUrls[0], uploadedImageUrls[1])
                        1 -> Images(uploadedImageUrls[0])
                        else -> Images()
                    }

                    val newService = Service(
                        title = title,
                        description = description,
                        price = price,
                        images = images
                    )
                    _isCreated.value = UiState.Success(repository.createService(token, newService))
                }
            }
        } catch (e: Exception) {
            _isCreated.value = UiState.Error("Gagal membuat layanan, coba beberapa saat lagi")
        }
    }

    fun updateService(
        id: String,
        token: String,
        title: String,
        description: String,
        price: Int,
        categoryId: Int,
        imagesUri: List<ImageUrl>,
        files: List<File?>
    ) {
        _isCreated.value = UiState.Loading
        try {
            viewModelScope.launch {
                val stringUrls = imagesUri.map { it.url.toString() }.filter { it.contains("https://storage.googleapis.com/karira/") }
                val uploadedImageUrls = mutableListOf<String>().apply { addAll(stringUrls) }
                files.map { file ->
                    async(Dispatchers.IO) {
                        file?.let {
                            val response = repository.uploadPhoto(token, file)
                            uploadedImageUrls.add(response)
                            return@async response
                        }
                    }
                }.awaitAll()

                async(Dispatchers.IO) {
                    val images = when (uploadedImageUrls.size) {
                        3 -> Images(uploadedImageUrls[0], uploadedImageUrls[1], uploadedImageUrls[2])
                        2 -> Images(uploadedImageUrls[0], uploadedImageUrls[1])
                        1 -> Images(uploadedImageUrls[0])
                        else -> Images()
                    }

                    val newService = Service(
                        title = title,
                        description = description,
                        price = price,
                        images = images,
                        category = Category(categoryId)
                    )

                    val data = repository.updateService(token, id, newService)
                    _isCreated.value = UiState.Success(data)
                }

            }
        } catch (e: Exception) {
            _isCreated.value = UiState.Error("Gagal mengedit layanan, coba beberapa saat lagi")
        }

    }

}