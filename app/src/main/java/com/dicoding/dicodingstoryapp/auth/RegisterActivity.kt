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
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.databinding.ActivityRegisterBinding
import com.dicoding.dicodingstoryapp.viewmodel.AuthViewModel
import com.dicoding.dicodingstoryapp.viewmodel.AuthViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setMyButtonEnable()

        binding.edRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        authViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            authViewModel.registerAccount(name, email, password).observe(this) {}
            authViewModel.getSuccessMessage().observe(this) { successMessage ->
                if (!successMessage.isNullOrEmpty()) {
                    val alertDialogBuilder = AlertDialog.Builder(this@RegisterActivity)
                    alertDialogBuilder.setTitle(getString(R.string.success))
                    alertDialogBuilder.setMessage(successMessage as CharSequence?)
                    alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    val alertDialog: AlertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            }

            authViewModel.getErrorMessage().observe(this) { errorMessage ->
                if (!errorMessage.isNullOrEmpty()) {
                    val alertDialogBuilder = AlertDialog.Builder(this@RegisterActivity)
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
        val email = binding.edRegisterEmail.text
        val password = binding.edRegisterPassword.text

        if (email != null && password != null) {
            binding.btnRegister.isEnabled =
                email.toString().isNotEmpty() && password.toString().isNotEmpty()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val registerTitle = ObjectAnimator.ofFloat(binding.tvJoin, View.ALPHA, 1f).setDuration(300)
        val registerDesc = ObjectAnimator.ofFloat(binding.tvFill, View.ALPHA, 1f).setDuration(300)
        val fullname =
            ObjectAnimator.ofFloat(binding.txtLayoutName, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.txtLayoutEmail, View.ALPHA, 1f).setDuration(300)
        val password =
            ObjectAnimator.ofFloat(binding.txtLayoutPassword, View.ALPHA, 1f).setDuration(300)
        val btnLogin =
            ObjectAnimator.ofFloat(binding.layoutBtnLogin, View.ALPHA, 1f).setDuration(300)
        val btnRegist = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(300)

        val togetherRegister = AnimatorSet().apply {
            playTogether(registerTitle, registerDesc)
        }
        AnimatorSet().apply {
            playSequentially(togetherRegister, fullname, email, password, btnRegist, btnLogin)
            start()
        }
    }
}
