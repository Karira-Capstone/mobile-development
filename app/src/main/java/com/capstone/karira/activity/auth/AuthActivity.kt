package com.capstone.karira.activity.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.karira.databinding.ActivityAuthBinding
import com.capstone.karira.model.Freelancer
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.auth.AuthViewModel


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

    fun saveUser(userDataStore: UserDataStore) = authViewModel.saveUser(userDataStore)

    fun addUserRole(role: String) = authViewModel.addUserRole(role)

    fun addUserSkill(skill: String) = authViewModel.addUserSkill(skill)

    fun removeUserSkill(skill: String) = authViewModel.removeUserSkill(skill)

    fun deleteUser() = authViewModel.deleteUser()

    fun activateUser() = authViewModel.activateUser()

    suspend fun authenticate(idToken: String) = authViewModel.authenticate(idToken)

    suspend fun createFreelancer(idToken: String) = authViewModel.createFreelancer(idToken)

    suspend fun createClient(idToken: String) = authViewModel.createClient(idToken)

    suspend fun updateFreelancer(token: String, freelancer: Freelancer) = authViewModel.updateFreelancer(token, freelancer)

    suspend fun getUserProfile(token: String) = authViewModel.getUserProfile(token)

    companion object {
        private const val TAG = "AuthActivity"
    }
}