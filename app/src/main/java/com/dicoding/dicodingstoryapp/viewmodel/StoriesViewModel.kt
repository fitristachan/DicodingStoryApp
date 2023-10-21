package com.dicoding.dicodingstoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingstoryapp.repository.StoriesRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    val isLoading: LiveData<Boolean> = storiesRepository.isLoading

    fun addStory(
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?,
        file: MultipartBody.Part
    ) =
        storiesRepository.addStory(description, lat, lon, file)

    fun getStoriesWithLocation() = storiesRepository.getStoriesWithLocation()

    fun getSuccessMessage(): LiveData<String> = storiesRepository.successMessage
    fun getErrorMessage(): LiveData<String> = storiesRepository.errorMessage
}