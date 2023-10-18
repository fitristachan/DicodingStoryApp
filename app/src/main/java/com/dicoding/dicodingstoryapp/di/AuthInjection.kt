package com.dicoding.dicodingstoryapp.di

import android.content.Context
import com.dicoding.dicodingstoryapp.repository.AuthRepository
import com.dicoding.dicodingstoryapp.retrofit.api.ApiConfig

object AuthInjection {
    fun provideRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getApiService("")
        return AuthRepository.getInstance(apiService)
    }
}