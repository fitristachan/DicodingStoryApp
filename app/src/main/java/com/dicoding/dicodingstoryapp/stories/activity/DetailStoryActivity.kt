package com.dicoding.dicodingstoryapp.stories.activity

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import com.dicoding.dicodingstoryapp.databinding.ActivityDetailStoryBinding
import com.dicoding.dicodingstoryapp.stories.activity.MainActivity.Companion.loadImage

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = ""
        supportActionBar?.elevation = 0F

        val typedValue = TypedValue()
        theme.resolveAttribute(
            com.google.android.material.R.attr.colorOnPrimary, typedValue, true
        )
        supportActionBar?.setBackgroundDrawable(ColorDrawable(typedValue.data))

        val imageUrl = intent.getStringExtra("imageUrl")
        val title = intent.getStringExtra("title")
        val desc = intent.getStringExtra("desc")

        binding.ivDetailPhoto.loadImage(imageUrl)
        binding.tvDetailName.text = title
        binding.tvDetailDescription.text = desc
    }
}