package com.capstone.karira.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class AuthenticateRequest (
    @field:SerializedName("idToken")
    val idToken: String,
)