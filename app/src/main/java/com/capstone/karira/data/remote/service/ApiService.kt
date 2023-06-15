package com.capstone.karira.data.remote.service

import com.capstone.karira.data.remote.model.request.AuthenticateRequest
import com.capstone.karira.data.remote.model.request.RecommendationRequest
import com.capstone.karira.data.remote.model.response.AuthenticateResponse
import com.capstone.karira.data.remote.model.response.RecommendationResponse
import com.capstone.karira.model.Bid
import com.capstone.karira.model.Client
import com.capstone.karira.model.Freelancer
import com.capstone.karira.model.Order
import com.capstone.karira.model.Notification
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.User
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    // --------------------------------------- AUTH --------------------------------------------------

    @POST("authenticate")
    suspend fun authenticate(
        @Body data: AuthenticateRequest,
    ): AuthenticateResponse

    // -------------------------------------------- USER ---------------------------------------------------------------

    @GET("users/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
    ): User

    @GET("users/recommendations")
    suspend fun getUserServiceRecommendation(
        @Header("Authorization") token: String,
    ): List<Service>

    @GET("users/recommendations")
    suspend fun getUserProjectRecommendation(
        @Header("Authorization") token: String,
    ): List<Project>

    // ------------------------------------------ FREELANCER / WORKERS --------------------------------------------------

    @POST("workers")
    suspend fun createFreelancer(
        @Header("Authorization") token: String,
        @Body data: Freelancer? = Freelancer(),
    ): Freelancer

    @PUT("workers")
    suspend fun updateFreelancer(
        @Header("Authorization") token: String,
        @Body data: Freelancer? = Freelancer(),
    ): Freelancer

    @POST("workers/bids/projects/{id}")
    suspend fun createBid(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body data: Bid? = Bid(),
    ): Bid

    // ------------------------------------------ CLIENT --------------------------------------------------

    @POST("clients")
    suspend fun createClient(
        @Header("Authorization") token: String,
        @Body data: Client? = Client(),
    ): Client

    // ------------------------------------------ UPLOAD FILE --------------------------------------------------

    @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
    ): List<String>

    // -------------------------------------------------- SERVICES ----------------------------------------------

    @GET("services")
    suspend fun searchServices(
        @Query("q") q: String? = ""
    ): List<Service>

    @GET("services/{id}")
    suspend fun getService(
        @Path("id") id: String
    ): Service

    @POST("services")
    suspend fun createService(
        @Header("Authorization") token: String,
        @Body data: Service
    ): Service

    @PUT("services/{id}")
    suspend fun updateService(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body data: Service
    ): Service

    // -------------------------------------------------- PROJECTS ----------------------------------------------

    @GET("projects")
    suspend fun searchProjects(
        @Query("q") q: String? = ""
    ): List<Project>

    @GET("projects/{id}")
    suspend fun getProject(
        @Path("id") id: String
    ): Project

    @POST("projects")
    suspend fun createProject(
        @Header("Authorization") token: String,
        @Body data: Project
    ): Project

    @PUT("projects/{id}")
    suspend fun updateProject(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body data: Project
    ): Project

    // ----------------------------------------- ORDERS ---------------------------------------------------

    @GET("orders/{id}")
    suspend fun getOrder(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Order

    @POST("orders/projects/bids/{id}")
    suspend fun createOrderFromProjectBid(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body data: Order
    ): Order

    @POST("orders/services/{id}")
    suspend fun createOrderFromService(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body data: Order
    ): Order

    @GET("users/orders")
    suspend fun getOrderByUser(
        @Header("Authorization") token: String
    ): List<Order>

    @PUT("orders/{id}/cancel")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Order

    @PUT("orders/{id}/finish")
    suspend fun finishOrder(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Order

    // --------------------------------------- RECCOMENDATION ------------------------------------------------

    @POST("recommendation/service-budget")
    suspend fun getServiceRecommendation(
        @Body data: RecommendationRequest
    ): RecommendationResponse

    @POST("recommendation/project-budget")
    suspend fun getProjectRecommendation(
        @Body data: RecommendationRequest
    ): RecommendationResponse

    // ---------------------------------------------- NOTIFICATIONS ---------------------------------------------

    @GET("users/notifications")
    suspend fun getNotifications(
        @Header("Authorization") token: String
    ): List<Notification>

    // ---------------------------------------------- TRANSAKSI --------------------------------------------------

    @GET("users/orders?status=FINISHED&status=CANCELLED")
    suspend fun getRiwayatTransactions(
        @Header("Authorization") token: String
    ): List<Order>

    @GET("users/orders?status=CREATED&status=ACCEPTED&status=PAID")
    suspend fun getProsesTransactions(
        @Header("Authorization") token: String
    ): List<Order>

}