package com.dicoding.dicodingstoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingstoryapp.datastore.UserPreference
import kotlinx.coroutines.launch

class UserPreferenceViewModel(private val pref: UserPreference) : ViewModel() {
    fun getSession(): LiveData<Boolean?> {
        return pref.getSession().asLiveData()
    }

    fun getName(): LiveData<String?> {
        return pref.getName().asLiveData()
    }

    fun saveToken(token: String, session: Boolean, name: String) {
        viewModelScope.launch {
            pref.saveUser(token, session, name)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            pref.deleteAll()
        }
    }
}