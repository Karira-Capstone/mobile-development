package com.capstone.karira.component.xml.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.karira.R
import com.capstone.karira.activity.auth.AuthActivity
import com.capstone.karira.databinding.FragmentHeadingCardsBinding
import com.capstone.karira.databinding.FragmentSelectRoleBinding

class HeadingCardsFragment : Fragment() {

    private var _binding: FragmentHeadingCardsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHeadingCardsBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleContent()
    }

    fun handleContent() {
        val type = arguments?.getString(TYPE).toString()

        when (type) {
            "LAYANAN MAIN" -> {
                binding.headingTitle.text = getString(R.string.layanan_recommend_title)
            }
            else -> {}
        }
    }

    companion object {
        const val TYPE = "TYPE"
    }

}