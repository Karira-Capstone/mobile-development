package com.capstone.karira.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.capstone.karira.R
import com.capstone.karira.activity.dashboard.home.HomeFragment
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

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }


}