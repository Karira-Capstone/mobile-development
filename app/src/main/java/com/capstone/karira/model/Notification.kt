package com.capstone.karira.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Notification(
    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("created_at"   ) var createdAt   : Date? = null,
    @SerializedName("type"         ) var type        : String? = null,
    @SerializedName("title"        ) var title       : String? = null,
    @SerializedName("description"  ) var description : String? = null,
    @SerializedName("is_seen"      ) var isSeen      : Boolean?= null,
    @SerializedName("user_id"      ) var userId      : String? = null,
)
