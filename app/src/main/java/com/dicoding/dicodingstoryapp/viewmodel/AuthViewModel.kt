package com.dicoding.dicodingstoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingstoryapp.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {
   fun registerAccount(name: String, email: String, password: String) = authRepository.registerAccount(name, email, password)

   fun loginAccount(email: String, password: String) = authRepository.loginAccount(email, password)

   fun getSuccessMessage(): LiveData<String> = authRepository.successMessage

   fun getErrorMessage():LiveData<String> = authRepository.errorMessage

   val isLoading: LiveData<Boolean> = authRepository.isLoading

}