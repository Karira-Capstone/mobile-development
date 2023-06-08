package com.capstone.karira.data.remote.model.response

import com.capstone.karira.model.User
import com.google.gson.annotations.SerializedName

data class AuthenticateResponse(
    @SerializedName("token") var token: String? = null,
    @SerializedName("user" ) var user : User?   = User()
)