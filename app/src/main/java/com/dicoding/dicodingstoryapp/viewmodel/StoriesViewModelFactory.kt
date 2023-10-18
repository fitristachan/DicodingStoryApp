package com.dicoding.dicodingstoryapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingstoryapp.di.StoriesInjection
import com.dicoding.dicodingstoryapp.repository.StoriesRepository

class StoriesViewModelFactory(private val storiesRepository: StoriesRepository):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoriesViewModel::class.java)) {
            return StoriesViewModel(storiesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoriesViewModelFactory? = null
        fun getInstance(context: Context): StoriesViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoriesViewModelFactory(StoriesInjection.provideRepository(context))
            }.also { instance = it }
    }
}