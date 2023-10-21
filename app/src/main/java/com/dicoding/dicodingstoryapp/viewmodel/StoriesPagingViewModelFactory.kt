package com.dicoding.dicodingstoryapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingstoryapp.di.StoriesPagingInjection
import com.dicoding.dicodingstoryapp.repository.StoriesPagingRepository

class StoriesPagingViewModelFactory(private val storiesPagingRepository: StoriesPagingRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoriesPagingViewModel::class.java)) {
            return StoriesPagingViewModel(storiesPagingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoriesPagingViewModelFactory? = null
        fun getInstance(context: Context): StoriesPagingViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoriesPagingViewModelFactory(
                    StoriesPagingInjection.provideRepository(
                        context
                    )
                )
            }.also { instance = it }
    }
}