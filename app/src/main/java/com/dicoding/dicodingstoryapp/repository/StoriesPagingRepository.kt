package com.dicoding.dicodingstoryapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.dicodingstoryapp.retrofit.api.ApiService
import com.dicoding.dicodingstoryapp.retrofit.response.ListStoryItem
import com.dicoding.dicodingstoryapp.room.StoriesPagingDatabase
import com.dicoding.dicodingstoryapp.room.StoriesRemoteMediator

class StoriesPagingRepository(
    private val storiesPagingDatabase: StoriesPagingDatabase, private val apiService: ApiService
) {
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class) return Pager(config = PagingConfig(
            pageSize = 5
        ),
            remoteMediator = StoriesRemoteMediator(storiesPagingDatabase, apiService),
            pagingSourceFactory = {
                storiesPagingDatabase.storiesPagingDao().getAllStories()
            }).liveData
    }

    companion object {
        @Volatile
        private var instance: StoriesPagingRepository? = null
        fun getInstance(
            storiesPagingDatabase: StoriesPagingDatabase, apiService: ApiService
        ): StoriesPagingRepository = instance ?: synchronized(this) {
            instance ?: StoriesPagingRepository(storiesPagingDatabase, apiService)
        }.also { instance = it }
    }

}