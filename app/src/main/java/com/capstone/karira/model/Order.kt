package com.capstone.karira.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Order(

    @SerializedName("id"          ) var id          : Int?    = null,

    @SerializedName("title"       ) var title       : String? = null,
    @SerializedName("name"        ) var name        : String? = null,
    @SerializedName("created_at"  ) var createdAt   : Date?   = null,

    @SerializedName("description" ) var description : String? = null,
    @SerializedName("price"       ) var price       : Int?    = null,
    @SerializedName("status"      ) var status      : String? = null,
    @SerializedName("type"        ) var type        : String? = null,
    @SerializedName("attachment"  ) var attachment  : String? = null,

    @SerializedName("bid_id"      ) var bidId       : Int?    = null,
    @SerializedName("project_id"  ) var projectId   : Int?    = null,
    @SerializedName("service_id"  ) var serviceId   : Int?    = null,
    @SerializedName("worker_id"   ) var workerId    : Int?    = null,
    @SerializedName("client_id"   ) var clientId    : Int?    = null,

    @SerializedName("bid"         ) var bid         : Bid?    = null,
    @SerializedName("project"     ) var project     : Project?      = null,
    @SerializedName("service"     ) var service     : Service?      = null,
    @SerializedName("worker"      ) var worker      : Freelancer?   = null,
    @SerializedName("client"      ) var client      : Client?       = null,

    @SerializedName(" midtrans_redirect_url") var midtransRedirectUrl : String? = null,

    )
