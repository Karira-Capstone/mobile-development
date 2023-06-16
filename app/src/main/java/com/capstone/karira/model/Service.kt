package com.capstone.karira.model

import android.provider.MediaStore
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Service (
    @SerializedName("id"             ) var id           : Int?    = null,
    @SerializedName("created_at"     ) var createdAt    : String? = null,
    @SerializedName("last_updated"   ) var lastUpdated  : String? = null,
    @SerializedName("type"           ) var type         : String? = null,
    @SerializedName("title"          ) var title        : String? = null,
    @SerializedName("price"          ) var price        : Int?    = null,
    @SerializedName("duration"       ) var duration     : Int?    = null,
    @SerializedName("images"         ) var images       : Images? = Images(),
    @SerializedName("description"    ) var description  : String? = null,
    @SerializedName("avg_rating"     ) var avgRating    : Int?    = null,
    @SerializedName("num_of_reviews" ) var numOfReviews : Int?    = null,
    @SerializedName("worker_id"      ) var workerId     : Int?    = null,
    @SerializedName("category_id"    ) var categoryId   : Int?    = null,
    @SerializedName("skills"         ) val skills       : List<Skills>? = null,
    @SerializedName("worker"         ) var worker       : Freelancer?   = null,
    @SerializedName("category"       ) var category     : Category?     = null,
    @SerializedName("orders"         ) val orders       : List<Order>?  = listOf()
)