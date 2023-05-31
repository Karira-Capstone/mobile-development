package com.capstone.karira.activity.auth

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.capstone.karira.R
import com.capstone.karira.databinding.ActivityAuthBinding
import com.capstone.karira.model.User
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    val authViewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun getUiState() = authViewModel.uiState

    fun getUserLiveData() = authViewModel.getUserLiveData()

    fun getUser() = authViewModel.getUser()

    fun saveUser(user: User) = authViewModel.saveUser(user)

    fun addUserRole(role: String) = authViewModel.addUserRole(role)

    fun addUserSkill(skill: String) = authViewModel.addUserSkill(skill)

    fun removeUserSkill(skill: String) = authViewModel.removeUserSkill(skill)

    fun activateUser() = authViewModel.activateUser()

    companion object {
        private const val TAG = "AuthActivity"
    }
}