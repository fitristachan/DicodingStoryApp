package com.dicoding.dicodingstoryapp.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.databinding.ActivityLoginBinding
import com.dicoding.dicodingstoryapp.datastore.UserPreference
import com.dicoding.dicodingstoryapp.datastore.dataStore
import com.dicoding.dicodingstoryapp.stories.activity.MainActivity
import com.dicoding.dicodingstoryapp.viewmodel.AuthViewModel
import com.dicoding.dicodingstoryapp.viewmodel.AuthViewModelFactory
import com.dicoding.dicodingstoryapp.viewmodel.UserPreferenceViewModel
import com.dicoding.dicodingstoryapp.viewmodel.UserPreferenceViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setMyButtonEnable()

        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        authViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            val pref = UserPreference.getInstance(application.dataStore)
            val userPreferenceViewModel =
                ViewModelProvider(this, UserPreferenceViewModelFactory(pref)).get(
                    UserPreferenceViewModel::class.java
                )

            authViewModel.loginAccount(email, password).observe(this) { login ->
                val token = login.loginResult?.token
                val name = login.loginResult?.name
                val session = true

                if (token != null) {
                    userPreferenceViewModel.saveToken(token, session, name.toString())
                }
            }

            authViewModel.getSuccessMessage().observe(this) { successMessage ->
                if (successMessage.isNotEmpty()) {
                    val alertDialogBuilder = AlertDialog.Builder(this@LoginActivity)
                    alertDialogBuilder.setTitle(getString(R.string.success))
                    alertDialogBuilder.setMessage(successMessage as CharSequence?)
                    alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        dialog.dismiss()
                    }

                    val alertDialog: AlertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            }

            authViewModel.getErrorMessage().observe(this) { errorMessage ->
                if (errorMessage.isNotEmpty()) {
                    val alertDialogBuilder = AlertDialog.Builder(this@LoginActivity)
                    alertDialogBuilder.setTitle(getString(R.string.error_title))
                    alertDialogBuilder.setMessage(errorMessage as CharSequence?)
                    alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val alertDialog: AlertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setMyButtonEnable() {
        val email = binding.edLoginEmail.text
        val password = binding.edLoginPassword.text

        if (email != null && password != null) {
            binding.btnLogin.isEnabled =
                email.toString().isNotEmpty() && password.toString().isNotEmpty()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val loginTitle = ObjectAnimator.ofFloat(binding.tvJoin, View.ALPHA, 1f).setDuration(300)
        val loginDesc = ObjectAnimator.ofFloat(binding.tvFill, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.txtLayoutEmail, View.ALPHA, 1f).setDuration(300)
        val password =
            ObjectAnimator.ofFloat(binding.txtLayoutPassword, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(300)
        val btnRegist =
            ObjectAnimator.ofFloat(binding.layoutBtnRegist, View.ALPHA, 1f).setDuration(300)

        val togetherLogin = AnimatorSet().apply {
            playTogether(loginTitle, loginDesc)
        }
        AnimatorSet().apply {
            playSequentially(togetherLogin, email, password, btnLogin, btnRegist)
            start()
        }
    }
}