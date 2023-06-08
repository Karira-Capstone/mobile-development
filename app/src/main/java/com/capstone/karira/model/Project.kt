package com.capstone.karira.model

import com.google.gson.annotations.SerializedName

data class Project(
    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("created_at"   ) var createdAt   : String? = null,
    @SerializedName("last_updated" ) var lastUpdated : String? = null,
    @SerializedName("title"        ) var title       : String? = null,
    @SerializedName("type"         ) var type        : String? = null,
    @SerializedName("lower_bound"  ) var lowerBound  : Int?    = null,
    @SerializedName("upper_bound"  ) var upperBound  : Int?    = null,
    @SerializedName("duration"     ) var duration    : Int?    = null,
    @SerializedName("description"  ) var description : String? = null,
    @SerializedName("attachment"   ) var attachment  : String? = null,
    @SerializedName("client_id"    ) var clientId    : Int?    = null,
    @SerializedName("category_id"  ) var categoryId  : Int?    = null,
    @SerializedName("client"       ) var client      : Client? = null,
    @SerializedName("category"       ) var category     : Category?    = null

)
