package com.dicoding.dicodingstoryapp.di

import android.content.Context
import com.dicoding.dicodingstoryapp.datastore.UserPreference
import com.dicoding.dicodingstoryapp.datastore.dataStore
import com.dicoding.dicodingstoryapp.repository.StoriesPagingRepository
import com.dicoding.dicodingstoryapp.retrofit.api.ApiConfig
import com.dicoding.dicodingstoryapp.room.StoriesPagingDatabase
import kotlinx.coroutines.runBlocking

object StoriesPagingInjection {
    fun provideRepository(context: Context): StoriesPagingRepository {
        val database = StoriesPagingDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val token = runBlocking { pref.getToken() }
        val apiService = ApiConfig.getApiService(token)
        return StoriesPagingRepository(database, apiService)
    }
}