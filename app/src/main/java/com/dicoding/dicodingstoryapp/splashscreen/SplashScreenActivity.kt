package com.dicoding.dicodingstoryapp.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.datastore.UserPreference
import com.dicoding.dicodingstoryapp.datastore.dataStore
import com.dicoding.dicodingstoryapp.landingpage.LandingActivity
import com.dicoding.dicodingstoryapp.stories.activity.MainActivity
import com.dicoding.dicodingstoryapp.viewmodel.UserPreferenceViewModel
import com.dicoding.dicodingstoryapp.viewmodel.UserPreferenceViewModelFactory

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val delayTime = 3000
        Handler(Looper.getMainLooper()).postDelayed({
            val pref = UserPreference.getInstance(application.dataStore)
            val userPreferenceViewModel: UserPreferenceViewModel by viewModels {
                UserPreferenceViewModelFactory(pref)
            }
            userPreferenceViewModel.getSession().observe(this) { session: Boolean? ->
                if (session == false) {
                    val intent = Intent(this, LandingActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }, delayTime.toLong())
    }
}