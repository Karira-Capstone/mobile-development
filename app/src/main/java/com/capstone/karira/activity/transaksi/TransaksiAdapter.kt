package com.capstone.karira.activity.transaksi

import android.R.attr
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.karira.databinding.ItemOrderBinding
import com.capstone.karira.model.Order
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale


class TransaksiAdapter(private var orders: ArrayList<Order>) : RecyclerView.Adapter<TransaksiAdapter.ViewHolder>() {

    fun updateOrders(newOrders: ArrayList<Order>) {
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (orders.isNotEmpty()) {
            val order = orders[position]
            holder.bind(order)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class ViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        private val titleTextView = binding.tvOrderTitle
        private val nameTextView = binding.tvOrderName
        private val createdAtTextView = binding.tvOrderCreatedAt
        private val statusTextView = binding.tvOrderStatus
        private val priceTextView = binding.tvOrderPrice

        fun bind(order: Order) {
            titleTextView.text = order.title
            nameTextView.text = order.name
            val dateFormatted = order.createdAt?.let { createdAt ->
                val locale = Locale("id", "ID")
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", locale)
                dateFormat.format(createdAt)
            }
            createdAtTextView.text = dateFormatted

            when (order.status) {
                "CREATED" -> statusTextView.text = "Pesanan dibuat"
                "ACCEPTED" -> statusTextView.text = "Pesanan diterima"
                "PAID" -> statusTextView.text = "Pesanan sudah dibayar"
                "FINISHED" -> statusTextView.text = "Pesanan selesai"
                "CANCELLED" -> statusTextView.text = "Pesanan dibatalkan"
                else -> statusTextView.text = ""
            }

            val price = order.price
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

            val formattedPrice = numberFormat.format(price)
                .replace(",00", "")

            priceTextView.text = formattedPrice
        }
    }
}