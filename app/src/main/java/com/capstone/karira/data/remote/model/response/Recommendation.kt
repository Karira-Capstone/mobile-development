package com.capstone.karira.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse (
    @field:SerializedName("result")
    val result: String,
)