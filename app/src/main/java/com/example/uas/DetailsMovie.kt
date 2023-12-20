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

        // Mendapatkan data dari Intent
        val title = intent.getStringExtra("title")
        val genre = intent.getStringExtra("genre")
        val description = intent.getStringExtra("description")
        val imgId = intent.getStringExtra("imgId")

        // Menampilkan data di tampilan
        binding.titleDetail.text = title
        binding.categoriesDetail.text = genre
        binding.descriptionDetail.text = description

        // Menggunakan Glide untuk memuat dan menampilkan gambar
        Glide.with(this)
            .load(imgId)
            .skipMemoryCache(true) // Skip caching in memory
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
            .into(binding.imageViewDetail)

        // Menambahkan listener untuk tombol kembali
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this@DetailsMovie, HomeUser::class.java))
        }
    }
}
