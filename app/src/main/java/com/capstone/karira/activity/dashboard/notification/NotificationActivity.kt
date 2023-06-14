package com.capstone.karira.activity.dashboard.notification

import NotificationAdapter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.karira.R
import com.capstone.karira.databinding.ActivityNotificationBinding
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.notification.NotificationViewModel
import kotlinx.coroutines.launch

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var adapter: NotificationAdapter
    private val viewModel: NotificationViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        setupRecyclerView()
        observeNotifications()
    }

    private fun setupActionBar() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        adapter = NotificationAdapter(ArrayList())
        binding.rvNotification.adapter = adapter
        binding.rvNotification.layoutManager = LinearLayoutManager(this)
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            viewModel.userDataStore.collect { userData ->
                val token = userData.token
                viewModel.getUserNotifications(token).observe(this@NotificationActivity) { notifications ->
                    notifications?.let {
                        adapter.updateNotifications(notifications)
                        if (notifications.isEmpty()) {
                            binding.tvNoItem.visibility = View.VISIBLE
                        } else {
                            binding.tvNoItem.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}