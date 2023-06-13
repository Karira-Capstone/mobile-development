package com.capstone.karira.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.capstone.karira.R
import com.capstone.karira.activity.dashboard.home.HomeFragment
import com.capstone.karira.activity.dashboard.notification.NotificationActivity
import com.capstone.karira.activity.transaksi.TransaksiFragment
import com.capstone.karira.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_account_circle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_transaction -> replaceFragment(TransaksiFragment())
                else -> {

                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu ?: return false
        menuInflater.inflate(R.menu.activity_main_toolbar, menu)
        return true
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

}