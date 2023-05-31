package com.capstone.karira.activity.layanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.karira.R
import com.capstone.karira.component.xml.fragment.ListFragment
import com.capstone.karira.component.xml.fragment.TitleFragment
import com.capstone.karira.databinding.ActivityLayananKuBinding

class LayananKuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayananKuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananKuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleFragmentContent()
    }

    fun handleFragmentContent() {
        val titleFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_top) as TitleFragment
        val listFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_bottom) as ListFragment

        val titleBundle = Bundle()

        titleBundle.putString(TitleFragment.TYPE, "LAYANAN KU")

        titleFragment.arguments = titleBundle
    }
}