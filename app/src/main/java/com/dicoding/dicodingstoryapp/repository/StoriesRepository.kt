package com.dicoding.dicodingstoryapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.dicodingstoryapp.retrofit.api.ApiService
import com.dicoding.dicodingstoryapp.retrofit.response.AddStoryResponse
import com.dicoding.dicodingstoryapp.retrofit.response.ErrorResponse
import com.dicoding.dicodingstoryapp.retrofit.response.StoriesResponse
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoriesRepository private constructor(
    private val apiService: ApiService
) {
    private val _storiesResponse = MutableLiveData<StoriesResponse>()
    private val storiesResponse: LiveData<StoriesResponse> = _storiesResponse

    private val _addStoryResponse = MutableLiveData<AddStoryResponse>()
    private val addStoryResponse: LiveData<AddStoryResponse> = _addStoryResponse

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addStory(
        description: RequestBody, lat: RequestBody?, lon: RequestBody?, file: MultipartBody.Part
    ): LiveData<AddStoryResponse> {
        _isLoading.value = true
        _errorMessage.value = ""
        _successMessage.value = ""

        val client = apiService.addStory(file, description, lat, lon)

        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>, response: Response<AddStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _addStoryResponse.value = response.body()
                    _successMessage.value = response.message().toString()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Add Story Fragment", "Error Body: $errorBody")

                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(), ErrorResponse::class.java
                    )
                    _errorMessage.value = errorResponse.message.toString()
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _isLoading.value = false
                val errorResponse = t.message
                _errorMessage.value = errorResponse.toString()
                Log.e("StoriesActivity", "onFailure: $errorResponse")
            }
        })
        return addStoryResponse
    }

    fun getStoriesWithLocation(): LiveData<StoriesResponse> {
        _isLoading.value = true
        _errorMessage.value = ""
        _successMessage.value = ""

        val client = apiService.getStoriesWithLocation()

        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>, response: Response<StoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storiesResponse.value = response.body()
                    _successMessage.value = response.message().toString()
                } else {
                    _errorMessage.value = response.errorBody()?.toString()
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message.toString()
            }
        })
        return storiesResponse
    }

    companion object {
        @Volatile
        private var instance: StoriesRepository? = null
        fun getInstance(
            apiService: ApiService
        ): StoriesRepository = instance ?: synchronized(this) {
            instance ?: StoriesRepository(apiService)
        }.also { instance = it }
    }
}