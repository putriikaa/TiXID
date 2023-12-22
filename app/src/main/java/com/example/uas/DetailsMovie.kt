package com.example.uas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uas.databinding.ActivityDetailsMovieBinding

class DetailsMovie : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val genre = intent.getStringExtra("genre")
        val description = intent.getStringExtra("description")
        val imgId = intent.getStringExtra("imgId")

        binding.titleDetail.text = title
        binding.categoriesDetail.text = genre
        binding.descriptionDetail.text = description

        Glide.with(this)
            .load(imgId)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imageViewDetail)

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this@DetailsMovie, HomeUser::class.java))
        }
    }
}
