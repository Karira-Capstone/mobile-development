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
    @SerializedName("category"     ) var category    : Category?    = null,
    @SerializedName("skills"       ) val skills      : List<Skills>? = null,
    @SerializedName("bids"         ) val bids        : List<Bid>? = null

)

data class Bid(
    @SerializedName("id"            ) var id          : Int?        = null,
    @SerializedName("price"         ) var price       : Int?        = null,
    @SerializedName("message"       ) var message     : String?     = null,
    @SerializedName("selected"      ) var selected    : Boolean?    = null,
    @SerializedName("created_at"    ) var createdAt   : String?     = null,
    @SerializedName("worker_id"     ) var workerId    : Int?        = null,
    @SerializedName("project_id"    ) var projectId   : Int?        = null,
    @SerializedName("worker"        ) var worker      : Freelancer? = null,
    @SerializedName("project"       ) var project     : Project?    = null,
//    @SerializedName("order"         ) val order       : Order? = null

)


