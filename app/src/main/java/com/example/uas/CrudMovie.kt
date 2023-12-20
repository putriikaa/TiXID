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
import com.example.uas.databinding.ActivityCrudMovieBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


class CrudMovie : AppCompatActivity() {
    private lateinit var binding: ActivityCrudMovieBinding
    private lateinit var itemAdapter: MovieAdapter
    private lateinit var itemList: ArrayList<Movie>
    private lateinit var recyclerViewItem: RecyclerView

    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movieadmin")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewItem = binding.rvMovie
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        itemList = arrayListOf()
        itemAdapter = MovieAdapter(itemList, movieadminCollectionRef)
        recyclerViewItem.adapter = itemAdapter

        with(binding) {
            fab.setOnClickListener {
                startActivity(Intent(this@CrudMovie, FormAddMovie::class.java))
            }

            btnLogoutAdmin.setOnClickListener {
                // Ubah nilai userType menjadi "guest" di SharedPreferences
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("userType", "guest")
                editor.apply()

                // Start activity login
                val intent = Intent(this@CrudMovie, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

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

                Log.d("CrudMovie Dataa","${snapshot.toString()}")
                Log.d("CrudMovie Dataa","${snapshot.toString()}")
                Log.d("CrudMovie Dataa","${itemList.toString()}")
                Log.d("CrudMovie Dataa","${itemList.toString()}")
                Log.d("CrudMovie", "Data retrieved successfully. Size: ${itemList.size}")

                Log.d("CrudMovie", "Data retrieved successfully. Size: ${itemList.size}")
            } else {
                Log.d("CrudMovie", "No data found.")
            }
        }





    }
}

