package com.capstone.karira.viewmodel.notification

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.capstone.karira.data.repository.NotificationRepository
import com.capstone.karira.model.Notification
import com.capstone.karira.model.UserDataStore
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getUserNotifications(token: String) = liveData {
        val initialNotifications = ArrayList<Notification>()
        emit(initialNotifications)

        try {
            val notifications = repository.getUserNotifications(token)
            initialNotifications.addAll(notifications)
            emit(initialNotifications)
        } catch (exception: Exception) {
            handleError(exception)
        }
    }

    private fun handleError(t: Throwable) {
        val errorMessage = when (t) {
            is IOException -> "Network error: Please check your internet connection"
            is HttpException -> "Server error: ${t.message()} with code ${t.code()}"
            else -> "Unknown error: ${t.message}"
        }
        Log.e(ContentValues.TAG, errorMessage, t)
    }
}


