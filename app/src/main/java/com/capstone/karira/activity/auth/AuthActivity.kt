package com.capstone.karira.activity.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.karira.R
import com.capstone.karira.databinding.ActivityAuthBinding
import com.capstone.karira.model.User
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth


class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    val authViewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun getUiState() = authViewModel.uiState

    fun getUser() = authViewModel.getUser()

    fun saveUser(user: User) = authViewModel.saveUser(user)

    companion object {
        private const val TAG = "AuthActivity"
    }
}