package com.capstone.karira.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.karira.di.Injection
import com.capstone.karira.viewmodel.auth.AuthViewModel
import com.capstone.karira.viewmodel.layanan.LayananBuatViewModel
import com.capstone.karira.viewmodel.layanan.LayananDetailViewModel
import com.capstone.karira.viewmodel.layanan.LayananKuViewModel
import com.capstone.karira.viewmodel.layanan.LayananMainViewModel
import com.capstone.karira.viewmodel.layanan.LayananSearchViewModel
import com.capstone.karira.viewmodel.rekomendasi.RekomendasiViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Auth
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(Injection.provideAuthRepostory(context)) as T
        }

        // Layanan
        else if (modelClass.isAssignableFrom(LayananMainViewModel::class.java)){
            return LayananMainViewModel(Injection.provideLayananRepostory(context)) as T
        } else if (modelClass.isAssignableFrom(LayananSearchViewModel::class.java)){
            return LayananSearchViewModel(Injection.provideLayananRepostory(context)) as T
        } else if (modelClass.isAssignableFrom(LayananKuViewModel::class.java)){
            return LayananKuViewModel(Injection.provideLayananRepostory(context)) as T
        } else if (modelClass.isAssignableFrom(LayananBuatViewModel::class.java)){
            return LayananBuatViewModel(Injection.provideLayananRepostory(context)) as T
        } else if (modelClass.isAssignableFrom(LayananDetailViewModel::class.java)){
            return LayananDetailViewModel(Injection.provideLayananRepostory(context)) as T
        }

        // Rekomendasi
        else if (modelClass.isAssignableFrom(RekomendasiViewModel::class.java)){
            return RekomendasiViewModel(Injection.provideAuthRepostory(context)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(context)
            }.also { instance = it }
    }

}