package com.nadikarim.submission2.data.remote

import com.nadikarim.submission2.data.model.login.LoginResponse
import com.nadikarim.submission2.data.model.login.RegisterResponse
import com.nadikarim.submission2.data.model.stories.AddResponse
import com.nadikarim.submission2.data.model.stories.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field ("name") name: String,
        @Field ("email") email: String,
        @Field ("password") password: String
    ) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password")password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser2(
        @Field("email") email: String,
        @Field("password")password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getListStory(
        @Header("Authorization") auth: String,
        @Query("page")page: Int,
        @Query("size")size: Int
    ): Response<StoriesResponse>

    @GET("stories")
    suspend fun getListStory2(
        @Header("Authorization") auth: String,
        @Query("page")page: Int,
        @Query("size")size: Int
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddResponse>

    @GET("stories")
    fun getListStoryWithLocation(
        @Header("Authorization") auth: String,
        @Query("location")location: Int
    ): Call<StoriesResponse>

    @GET("stories")
    fun getListStoryWithLocation2(
        @Header("Authorization") auth: String,
        @Query("location")location: Int
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun uploadImageWithLocation(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat")latitude: Float,
        @Part("lon")longitude: Float
    ): Call<AddResponse>

}