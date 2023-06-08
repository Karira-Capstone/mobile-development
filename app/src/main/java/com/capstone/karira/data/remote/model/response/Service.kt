package com.capstone.karira.data.remote.model.response

import com.capstone.karira.model.Service
import com.google.gson.annotations.SerializedName

data class SearchServiceResponse (
    val serviceList: List<Service>,
)