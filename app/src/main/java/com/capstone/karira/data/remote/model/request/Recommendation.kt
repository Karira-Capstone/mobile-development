package com.capstone.karira.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class RecommendationRequest (
    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("description")
    val description: String,
)