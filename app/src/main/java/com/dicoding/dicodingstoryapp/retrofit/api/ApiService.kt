package com.dicoding.dicodingstoryapp.retrofit.api

import com.dicoding.dicodingstoryapp.retrofit.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String, @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1, @Query("size") size: Int = 20
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): Call<AddStoryResponse>

    @GET("stories")
    fun getStoriesWithLocation(
        @Query("location") location: Int = 1,
    ): Call<StoriesResponse>
}