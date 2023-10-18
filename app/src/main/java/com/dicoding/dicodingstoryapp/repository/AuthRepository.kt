package com.dicoding.dicodingstoryapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.dicodingstoryapp.retrofit.api.ApiService
import com.dicoding.dicodingstoryapp.retrofit.response.ErrorResponse
import com.dicoding.dicodingstoryapp.retrofit.response.LoginResponse
import com.dicoding.dicodingstoryapp.retrofit.response.RegisterResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository private constructor(
    private val apiService: ApiService
) {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    private val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _loginResponse = MutableLiveData<LoginResponse>()
    private val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun registerAccount(
        name: String, email: String, password: String
    ): LiveData<RegisterResponse> {
        _errorMessage.value = ""
        _successMessage.value = ""

        _isLoading.value = true

        val client = apiService.register(name, email, password)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>, response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                    _successMessage.value = response.message().toString()

                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(), ErrorResponse::class.java
                    )
                    _errorMessage.value = errorResponse.message.toString()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                val errorResponse = t.message
                _errorMessage.value = errorResponse.toString()
            }
        })
        return registerResponse
    }

    fun loginAccount(email: String, password: String): LiveData<LoginResponse> {
        _errorMessage.value = ""
        _successMessage.value = ""
        _isLoading.value = true

        val client = apiService.login(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                    _successMessage.value = response.message().toString()
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(), ErrorResponse::class.java
                    )
                    _errorMessage.value = errorResponse.message.toString()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                val errorResponse = t.message
                _errorMessage.value = errorResponse.toString()
                Log.e("Register Activity", "Error message: $errorResponse")
            }
        })
        return loginResponse
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService
        ): AuthRepository = instance ?: synchronized(this) {
            instance ?: AuthRepository(apiService)
        }.also { instance = it }
    }
}