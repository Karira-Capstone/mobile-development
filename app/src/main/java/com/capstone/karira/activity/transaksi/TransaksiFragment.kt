package com.capstone.karira.activity.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.karira.databinding.FragmentTransaksiBinding

class TransaksiFragment : Fragment() {
    private lateinit var binding: FragmentTransaksiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransaksiBinding.inflate(inflater, container, false)

        return binding.root
    }
}