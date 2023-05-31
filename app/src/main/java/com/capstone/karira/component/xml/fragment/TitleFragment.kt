package com.capstone.karira.component.xml.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.karira.R
import com.capstone.karira.activity.layanan.LayananBuatActivity
import com.capstone.karira.activity.layanan.LayananDetailActivity
import com.capstone.karira.activity.layanan.LayananKuActivity
import com.capstone.karira.activity.layanan.LayananSearchActivity
import com.capstone.karira.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    private lateinit var type: String
    private var _binding: FragmentTitleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTitleBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type = arguments?.getString(TYPE).toString()

        handleContent()
        handleOnClickListener()
    }

    private fun handleContent() {
        when (type) {
            "LAYANAN MAIN" -> {
                binding.titleTitle.text = getString(R.string.layanan_title, "", "")
                binding.titleTitlesub.text = getString(R.string.layanan_titlesub)
                binding.titlePrimaryButton.text = getString(R.string.layanan_ku_button)
                binding.titleOutlinedButton.text = getString(R.string.layanan_cari_button)
                binding.titleOutlinedButton.visibility = View.VISIBLE
                binding.titleSearchLayout.visibility = View.GONE
            }
            "LAYANAN KU" -> {
                binding.titleTitle.text = getString(R.string.layanan_title, "", "ku")
                binding.titleTitlesub.text = getString(R.string.layanan_titlesub_ku)
                binding.titlePrimaryButton.text = getString(R.string.layanan_buat_button)
                binding.titleOutlinedButton.visibility = View.GONE
                binding.titleSearchLayout.visibility = View.GONE
            }
            "LAYANAN SEARCH" -> {
                binding.titleTitle.text = getString(R.string.layanan_title, "Cari ", "")
                binding.titleTitlesub.text = getString(R.string.layanan_titlesub_search)
                binding.titleButtonLayout.visibility = View.GONE
            }
            "LAYANAN BUAT" -> {
                binding.titleTitle.text = getString(R.string.layanan_title, "Buat ", "")
                binding.titleTitlesub.text = getString(R.string.layanan_titlesub_buat)
                binding.titleButtonLayout.visibility = View.GONE
                binding.titleSearchLayout.visibility = View.GONE
            }
            else -> {}
        }

    }

    private fun handleOnClickListener() {
        when (type) {
            "LAYANAN MAIN" -> {
                binding.titleOutlinedButton.setOnClickListener {
                    val intent = Intent(requireActivity(), LayananDetailActivity::class.java)
                    startActivity(intent)
                }
                binding.titlePrimaryButton.setOnClickListener {
                    val intent = Intent(requireActivity(), LayananKuActivity::class.java)
                    startActivity(intent)
                }
            }
            "LAYANAN KU" -> {
                binding.titlePrimaryButton.setOnClickListener {
                    val intent = Intent(requireActivity(), LayananBuatActivity::class.java)
                    startActivity(intent)
                }
            }
            "LAYANAN SEARCH" -> {
                handleTextListener()
//                binding.titleButtonLayout.visibility = View.GONE
            }
            else -> {}
        }
    }

    private fun handleTextListener() {
        setMyButtonEnable()

        binding.skillTextview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.skillSearchButton.setOnClickListener {
            handleSearch()
        }
    }

    private fun setMyButtonEnable() {
        val searchText = binding.skillTextview.text

        binding.skillSearchButton.isEnabled = searchText.isNotEmpty()
    }

    private fun handleSearch() {
        val searchText = binding.skillTextview.text

        // backend...
    }


    companion object {
        const val TYPE = "TYPE"
    }

}