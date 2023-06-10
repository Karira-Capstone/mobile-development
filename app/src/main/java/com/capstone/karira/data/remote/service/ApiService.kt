package com.capstone.karira.data.remote.service

import com.capstone.karira.data.remote.model.request.AuthenticateRequest
import com.capstone.karira.data.remote.model.request.SearchServiceRequest
import com.capstone.karira.data.remote.model.response.AuthenticateResponse
import com.capstone.karira.data.remote.model.response.SearchServiceResponse
import com.capstone.karira.model.Client
import com.capstone.karira.model.Freelancer
import com.capstone.karira.model.Project
import com.capstone.karira.model.Service
import com.capstone.karira.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
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

}