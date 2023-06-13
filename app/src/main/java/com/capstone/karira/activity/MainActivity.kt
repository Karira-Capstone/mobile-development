package com.capstone.karira.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.karira.R
import com.capstone.karira.activity.dashboard.home.HomeFragment
import com.capstone.karira.activity.layanan.LayananKuFragment
import com.capstone.karira.activity.layanan.RekomendasiFragment
import com.capstone.karira.activity.proyek.ProyekKuFragment
import com.capstone.karira.databinding.ActivityMainBinding
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.viewmodel.MainViewModel
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.auth.AuthViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var user: UserDataStore
    val mainViewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: Fragment = supportFragmentManager.findFragmentById(R.id.mockup_frame_layout) as Fragment
        val navController = navHostFragment.findNavController()
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setupWithNavController(navController)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_account_circle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observeLiveData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main_toolbar, menu)
        return true
    }

    private fun observeLiveData() {
        mainViewModel.getUserLiveData().observe(this) { userDataStore ->
            user = userDataStore

            val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

            if (user.role == "WORKER") navView.menu.findItem(R.id.layananKuFragment).isVisible = true
            else navView.menu.findItem(R.id.proyekKuFragment).isVisible = true
        }
    }

}