package com.capstone.karira.viewmodel.transaksi

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.capstone.karira.data.repository.TransaksiRepository
import com.capstone.karira.model.Notification
import com.capstone.karira.model.Order
import com.capstone.karira.model.UserDataStore
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class TransaksiViewModel(private val repository: TransaksiRepository) : ViewModel() {

    val userDataStore: Flow<UserDataStore> get() = repository.getUser()

    fun getRiwayatTransactions(token: String) = liveData {
        val initialTransactions = ArrayList<Order>()
        emit(initialTransactions)

        try {
            val transactions = repository.getRiwayatTransactions(token)
            initialTransactions.addAll(transactions)
            emit(initialTransactions)
        } catch (exception: Exception) {
            handleError(exception)
        }
    }

    fun getProsesTransactions(token: String) = liveData {
        val initialTransactions = ArrayList<Order>()
        emit(initialTransactions)

        try {
            val transactions = repository.getProsesTransactions(token)
            initialTransactions.addAll(transactions)
            emit(initialTransactions)
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