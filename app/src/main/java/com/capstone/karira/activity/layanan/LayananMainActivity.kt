package com.capstone.karira.activity.layanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.capstone.karira.R
import com.capstone.karira.component.xml.fragment.HeadingCardsFragment
import com.capstone.karira.component.xml.fragment.ListFragment
import com.capstone.karira.component.xml.fragment.TitleFragment
import com.capstone.karira.databinding.ActivityLayananMainBinding
import com.google.android.material.button.MaterialButton

class LayananMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayananMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleFragmentContent()
    }

    private fun handleFragmentContent() {
        val titleFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_top) as TitleFragment
        val headingCardsFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_center) as HeadingCardsFragment
        val listFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_bottom) as ListFragment

        val titleBundle = Bundle()
        val headingCardsBundle = Bundle()

        titleBundle.putString(TitleFragment.TYPE, "LAYANAN MAIN")
        headingCardsBundle.putString(HeadingCardsFragment.TYPE, "LAYANAN MAIN")

        titleFragment.arguments = titleBundle
        headingCardsFragment.arguments = headingCardsBundle
    }

}