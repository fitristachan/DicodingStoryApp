package com.dicoding.dicodingstoryapp.landingpage

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dicoding.dicodingstoryapp.auth.RegisterActivity
import com.dicoding.dicodingstoryapp.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val isFirstLaunch = preferences.getBoolean("isFirstLaunch", true)

        if (isFirstLaunch) {
            supportActionBar?.hide()
            binding = ActivityLandingBinding.inflate(layoutInflater)
            setContentView(binding.root)

            playAnimation()
            binding.btnNext.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            preferences.edit().putBoolean("isFirstLaunch", false).apply()
        } else {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLanding, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcomeTitle =
            ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(100)
        val welcomeDesc =
            ObjectAnimator.ofFloat(binding.tvWelcomeDesc, View.ALPHA, 1f).setDuration(100)
        val btnNext = ObjectAnimator.ofFloat(binding.btnNext, View.ALPHA, 1f).setDuration(100)
        val imgWave = ObjectAnimator.ofFloat(binding.imgWave, View.ALPHA, 1f).setDuration(100)
        val together = AnimatorSet().apply {
            playTogether(welcomeTitle, welcomeDesc)
        }
        AnimatorSet().apply {
            playSequentially(together, btnNext, imgWave)
            start()
        }
    }
}