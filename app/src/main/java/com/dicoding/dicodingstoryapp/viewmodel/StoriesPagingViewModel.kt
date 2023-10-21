package com.dicoding.dicodingstoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.dicodingstoryapp.repository.StoriesPagingRepository
import com.dicoding.dicodingstoryapp.retrofit.response.ListStoryItem

class StoriesPagingViewModel(storiesPagingRepository: StoriesPagingRepository) : ViewModel() {

    val getStories: LiveData<PagingData<ListStoryItem>> =
        storiesPagingRepository.getStories().cachedIn(viewModelScope)
}