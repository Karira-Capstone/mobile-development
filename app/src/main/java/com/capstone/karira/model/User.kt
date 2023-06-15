package com.capstone.karira.model

import com.google.gson.annotations.SerializedName

data class UserDataStore (
    val firebaseToken: String,
    val token: String,
    val role: String = "UNDEFINED",
    val skills: String = "",
    val isActivated: Boolean = false,
    val picture: String = "",
    val fullName: String = "",
    val id: String = ""
)

data class User (

    @SerializedName("id"           ) var id          : String?  = null,
    @SerializedName("email"        ) var email       : String?  = null,
    @SerializedName("full_name"    ) var fullName    : String?  = null,
    @SerializedName("role"         ) var role        : String?  = null,
    @SerializedName("picture"      ) var picture     : String?  = null,
    @SerializedName("created_at"   ) var createdAt   : String?  = null,
    @SerializedName("onboarded"    ) var onboarded   : Boolean? = null,
    @SerializedName("is_active"    ) var isActive    : Boolean? = null,
    @SerializedName("last_login"   ) var lastLogin   : String?  = null,
    @SerializedName("device_token" ) var deviceToken : String?  = null,
    @SerializedName("client"       ) var client      : Client?  = null,
    @SerializedName("worker"       ) var worker      : Freelancer?  = null,

)


data class Freelancer (

    @SerializedName("id"              ) var id             : Int?               = null,
    @SerializedName("phone"           ) var phone          : String?            = null,
    @SerializedName("identity_number" ) var identityNumber : String?            = null,
    @SerializedName("birth_date"      ) var birthDate      : String?            = null,
    @SerializedName("province"        ) var province       : String?            = null,
    @SerializedName("city"            ) var city           : String?            = null,
    @SerializedName("address"         ) var address        : String?            = null,
    @SerializedName("description"     ) var description    : String?            = null,
    @SerializedName("avg_rating"      ) var avgRating      : Int?               = null,
    @SerializedName("num_of_reviews"  ) var numOfReviews   : Int?               = null,
    @SerializedName("num_of_order"    ) var numOfOrder     : Int?               = null,
    @SerializedName("user_id"         ) var userId         : String?            = null,
    @SerializedName("user"            ) var user           : User?              = null,
    @SerializedName("skills"          ) var skills         : ArrayList<Skills>? = null,
    @SerializedName("orders"          ) val orders         : List<Order>? = listOf()

)

data class Client (

    @SerializedName("id"              ) var id             : Int?    = null,
    @SerializedName("phone"           ) var phone          : String? = null,
    @SerializedName("identity_number" ) var identityNumber : String? = null,
    @SerializedName("birth_date"      ) var birthDate      : String? = null,
    @SerializedName("province"        ) var province       : String? = null,
    @SerializedName("city"            ) var city           : String? = null,
    @SerializedName("address"         ) var address        : String? = null,
    @SerializedName("description"     ) var description    : String? = null,
    @SerializedName("avg_rating"      ) var avgRating      : Int?    = null,
    @SerializedName("num_of_reviews"  ) var numOfReviews   : Int?    = null,
    @SerializedName("user_id"         ) var userId         : String? = null,
    @SerializedName("user"            ) var user           : User?   = null,
    @SerializedName("orders"          ) val orders         : List<Order>? = listOf()

)

