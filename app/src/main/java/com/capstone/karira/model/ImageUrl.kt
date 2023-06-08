package com.capstone.karira.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class ImageUrl(
    val url: Uri
)

data class Images (

    @SerializedName("foto_1" ) var foto1 : String? = null,
    @SerializedName("foto_2" ) var foto2 : String? = null,
    @SerializedName("foto_3" ) var foto3 : String? = null

)