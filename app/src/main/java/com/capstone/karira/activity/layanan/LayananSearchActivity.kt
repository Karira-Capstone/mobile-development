package com.capstone.karira.activity.layanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.karira.R
import com.capstone.karira.component.xml.fragment.HeadingCardsFragment
import com.capstone.karira.component.xml.fragment.ListFragment
import com.capstone.karira.component.xml.fragment.TitleFragment
import com.capstone.karira.databinding.ActivityLayananMainBinding
import com.capstone.karira.databinding.ActivityLayananSearchBinding

class LayananSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayananSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleFragmentContent()
    }

    fun handleFragmentContent() {
        val titleFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_top) as TitleFragment
        val listFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_bottom) as ListFragment

        val titleBundle = Bundle()

        titleBundle.putString(TitleFragment.TYPE, "LAYANAN SEARCH")

        titleFragment.arguments = titleBundle
    }

}