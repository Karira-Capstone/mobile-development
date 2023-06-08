package com.capstone.karira.model

import com.google.gson.annotations.SerializedName

data class Skills (
    @SerializedName("id" ) var id : Int? = null
)

data class Category (
    @SerializedName("id" ) var id : Int? = null,
    @SerializedName("title" ) var title : String? = null
)