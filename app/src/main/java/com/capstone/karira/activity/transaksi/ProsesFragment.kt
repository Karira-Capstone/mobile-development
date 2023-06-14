package com.capstone.karira.activity.transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.karira.databinding.FragmentOrderBinding
import com.capstone.karira.viewmodel.ViewModelFactory
import com.capstone.karira.viewmodel.transaksi.TransaksiViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProsesFragment : Fragment() {

    private lateinit var adapter: TransaksiAdapter
    private val viewModel: TransaksiViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TransaksiAdapter(ArrayList())

        binding.rvOrder.adapter = adapter
        binding.rvOrder.layoutManager = LinearLayoutManager(requireContext())
        observeOrders()
    }

    private fun observeOrders() {
        lifecycleScope.launch {
            viewModel.userDataStore.collect { userData ->
                val token = userData.token
                viewModel.getProsesTransactions(token).observe(viewLifecycleOwner) { orders ->
                    orders?.let {
                        adapter.updateOrders(orders)
                        if (orders.isEmpty()) {
                            binding.tvNoTransaction.visibility = View.VISIBLE
                        } else {
                            binding.tvNoTransaction.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}