package com.example.uas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas.databinding.ActivityCrudMovieBinding
import com.google.firebase.firestore.FirebaseFirestore

class CrudMovie : AppCompatActivity() {

    private lateinit var binding: ActivityCrudMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private val channelId = "MOVIE_NOTIFICATION"
    private val notifId = 90

    // Firebase
    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movieadmin")

    companion object {
        const val FORM_ACTIVITY_REQUEST_CODE = 1
        const val DETAIL_ACTIVITY_REQUEST_CODE = 2
        const val EXTRA_UPDATE_ID = "extra_update_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_GENRE = "extra_genre"
        const val EXTRA_DESC = "extra_desc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.fab.setOnClickListener {
            val intent = Intent(this@CrudMovie, FormAddMovie::class.java)
            startActivityForResult(intent, FORM_ACTIVITY_REQUEST_CODE)
        }

        loadDataMovie()
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(emptyList(), object : MovieAdapter.OnEditClickListener {
            override fun onEditClick(movie: Movie) {
                val intent = Intent(this@CrudMovie, FormAddMovie::class.java).apply {
                    putExtra(EXTRA_UPDATE_ID, movie.id)
                    putExtra(EXTRA_TITLE, movie.judul)
                    putExtra(EXTRA_GENRE, movie.genre)
                    putExtra(EXTRA_DESC, movie.desc)
                }
                startActivityForResult(intent, DETAIL_ACTIVITY_REQUEST_CODE)
            }

            override fun onDeleteClick(movie: Movie) {
                deleteMovie(movie)
            }
        })

        binding.rvMovie.apply {
            layoutManager = LinearLayoutManager(this@CrudMovie)
            adapter = movieAdapter
        }
    }

    private fun loadDataMovie() {
        movieadminCollectionRef.get()
            .addOnSuccessListener { snapshot ->
                val movieList = mutableListOf<Movie>()
                for (document in snapshot) {
                    val movie = document.toObject(Movie::class.java)
                    movieList.add(movie)
                }

                // Log the size of the movie list for debugging
                Log.d("CrudMovie", "Movie List Size: ${movieList.size}")

                // Update the RecyclerView on the main thread
                runOnUiThread {
                    movieAdapter.updateData(movieList)
                }
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e("CrudMovie", "Error getting documents: $e")
            }
    }

    private fun deleteMovie(movie: Movie) {
        movieadminCollectionRef.document(movie.id)
            .delete()
            .addOnSuccessListener {
                // Handle success
                showNotification("Movie Deleted", "Movie ${movie.judul} has been deleted.")
                loadDataMovie() // Pastikan untuk memanggil loadDataMovie() setelah menghapus data
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    private fun showNotification(title: String, message: String) {
        val notifManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        val intent = Intent(this, CrudMovie::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, flag
        )

        val builder = NotificationCompat.Builder(
            this, channelId
        ).setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(
                channelId,
                "Movie Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notifManager.createNotificationChannel(notifChannel)
        }

        notifManager.notify(notifId, builder.build())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FORM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Data telah ditambahkan, muat ulang data
            loadDataMovie()
        } else if (requestCode == DETAIL_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle result from detail activity
            loadDataMovie() // Reload data after editing
        }
    }
}
