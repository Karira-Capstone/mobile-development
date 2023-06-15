package com.capstone.karira.activity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.karira.R
import com.capstone.karira.activity.dashboard.notification.NotificationActivity
import com.capstone.karira.databinding.ActivityMainBinding
import com.capstone.karira.model.UserDataStore
import com.capstone.karira.viewmodel.MainViewModel
import com.capstone.karira.viewmodel.ViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

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

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d(TAG, token)
        })

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



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_bar_notifications -> {
                startActivity(Intent(this, NotificationActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}