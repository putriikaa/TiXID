// Import berbagai komponen yang digunakan dalam kode
package com.example.uas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.database.Movie
import com.example.uas.databinding.ActivityCrudMovieBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

// Deklarasi kelas utama sebagai turunan dari AppCompatActivity
class CrudMovie : AppCompatActivity() {
    // Inisialisasi berbagai variabel dan objek yang diperlukan
    private lateinit var binding: ActivityCrudMovieBinding
    private lateinit var itemAdapter: MovieAdapter
    private lateinit var itemList: ArrayList<Movie>
    private lateinit var recyclerViewItem: RecyclerView
    private lateinit var prefManager: PrefManager
    private lateinit var auth: FirebaseAuth

    // Inisialisasi objek Firebase Firestore dan referensi koleksi 'movieadmin'
    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movieadmin")

    // Metode yang dipanggil saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi objek FirebaseAuth
        auth = Firebase.auth

        // Inisialisasi dan konfigurasi RecyclerView
        recyclerViewItem = binding.rvMovie
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        // Inisialisasi ArrayList dan adapter untuk RecyclerView
        itemList = arrayListOf()
        itemAdapter = MovieAdapter(itemList, movieadminCollectionRef)
        recyclerViewItem.adapter = itemAdapter
        prefManager = PrefManager.getInstance(this@CrudMovie)

        // Pengaturan tindakan klik pada FloatingActionButton dan tombol logout
        with(binding) {
            fab.setOnClickListener {
                startActivity(Intent(this@CrudMovie, FormAddMovie::class.java))
            }

            btnLogoutAdmin.setOnClickListener {
                auth.signOut()
                prefManager.setLoggedIn(false)
                prefManager.clear()

                val intent = Intent(this@CrudMovie, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }

        // Menambahkan pendengar untuk snapshot dari koleksi 'movieadmin'
        movieadminCollectionRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("CrudMovie", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                itemList.clear()
                for (document in snapshot.documents) {
                    val movie = document.toObject(Movie::class.java)
                    if (movie != null) {
                        itemList.add(movie)
                    }
                }
                itemAdapter.notifyDataSetChanged()

                Log.d("CrudMovie", "Data retrieved successfully. Size: ${itemList.size}")
            } else {
                Log.d("CrudMovie", "No data found.")
            }
        }
    }
}
