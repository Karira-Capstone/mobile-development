package com.capstone.karira.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SearchServiceRequest (
    @field:SerializedName("idToken")
    val idToken: String,
)