package com.capstone.karira.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.karira.di.Injection
import com.capstone.karira.viewmodel.auth.AuthViewModel
import com.capstone.karira.viewmodel.dashboard.home.HomeViewModel
import com.capstone.karira.viewmodel.layanan.LayananBuatViewModel
import com.capstone.karira.viewmodel.layanan.LayananDetailViewModel
import com.capstone.karira.viewmodel.layanan.LayananKuViewModel
import com.capstone.karira.viewmodel.layanan.LayananMainViewModel
import com.capstone.karira.viewmodel.layanan.LayananSearchViewModel
import com.capstone.karira.viewmodel.proyek.ProyekBuatViewModel
import com.capstone.karira.viewmodel.proyek.ProyekDetailViewModel
import com.capstone.karira.viewmodel.proyek.ProyekKuViewModel
import com.capstone.karira.viewmodel.proyek.ProyekMainViewModel
import com.capstone.karira.viewmodel.proyek.ProyekSearchViewModel
import com.capstone.karira.viewmodel.proyek.ProyekTawaranViewModel
import com.capstone.karira.viewmodel.rekomendasi.RekomendasiViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Auth Activity
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(Injection.provideAuthRepostory(context)) as T
        }

        // Main Activity
        else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(Injection.provideMainRepository(context)) as T
        }
        // Home
        else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(Injection.provideMainRepository(context)) as T
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

        // Proyek
        else if (modelClass.isAssignableFrom(ProyekMainViewModel::class.java)){
            return ProyekMainViewModel(Injection.provideProyekRepostory(context)) as T
        } else if (modelClass.isAssignableFrom(ProyekSearchViewModel::class.java)){
            return ProyekSearchViewModel(Injection.provideProyekRepostory(context)) as T
        } else if (modelClass.isAssignableFrom(ProyekKuViewModel::class.java)){
            return ProyekKuViewModel(Injection.provideProyekRepostory(context)) as T
        } else if (modelClass.isAssignableFrom(ProyekBuatViewModel::class.java)){
            return ProyekBuatViewModel(Injection.provideProyekRepostory(context)) as T
        } else if (modelClass.isAssignableFrom(ProyekDetailViewModel::class.java)){
            return ProyekDetailViewModel(Injection.provideProyekRepostory(context)) as T
        } else if (modelClass.isAssignableFrom(ProyekTawaranViewModel::class.java)){
            return ProyekTawaranViewModel(Injection.provideProyekRepostory(context)) as T
        }

        // Rekomendasi
        else if (modelClass.isAssignableFrom(RekomendasiViewModel::class.java)){
            return RekomendasiViewModel(Injection.provideRekomendasiRepostory(context)) as T
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