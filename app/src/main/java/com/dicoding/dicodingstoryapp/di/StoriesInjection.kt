package com.dicoding.dicodingstoryapp.di

import android.content.Context
import android.util.Log
import com.dicoding.dicodingstoryapp.datastore.dataStore
import com.dicoding.dicodingstoryapp.datastore.UserPreference
import com.dicoding.dicodingstoryapp.repository.StoriesRepository
import com.dicoding.dicodingstoryapp.retrofit.api.ApiConfig
import kotlinx.coroutines.runBlocking

object StoriesInjection {
    fun provideRepository(context: Context): StoriesRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val token = runBlocking { pref.getToken() }
        val apiService = ApiConfig.getApiService(token)
        Log.e("StoriesActivity", "apiService: $apiService")
        Log.e("StoriesActivity", "token: $token")
        return StoriesRepository.getInstance(apiService, pref)
    }
}