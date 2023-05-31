package com.capstone.karira.model

import java.util.Date

data class Service (
    val id: Int,
    val title: String,
    val max_duration: Int,
    val status: String,
    val images: String,
    val description: String,
    val price: String,
    val isNegotiable: Boolean = false,
    val usedBy: Int = 0,
    val last_updated: Date,
    val worker_id: Int = 0,
    val category_id: Int = 0,
)