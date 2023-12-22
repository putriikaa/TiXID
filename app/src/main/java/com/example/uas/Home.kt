package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.database.MovieDao
import com.example.uas.database.Movie
import com.example.uas.database.MovieRoomDatabase
import com.example.uas.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerViewItem : RecyclerView
    private lateinit var itemAdapter : UserAdapter
    private lateinit var itemList : ArrayList<Movie>
    private lateinit var movieDao: MovieDao

    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movieadmin")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        fetchDataFromFirestore()

        // Ambil nilai email dari data login (menggunakan Firebase Authentication sebagai contoh)
        val userEmail = getLoggedInUserEmail()

        // Set nilai email ke TextView
        binding.yourUsn.text = userEmail

        recyclerViewItem = binding.recyclerViewMovies
        recyclerViewItem.setHasFixedSize(true)

        itemList = arrayListOf()
        itemAdapter = UserAdapter(itemList, movieadminCollectionRef)
        recyclerViewItem.adapter = itemAdapter

        val db = MovieRoomDatabase.getDatabase(requireContext())!!.movieDao()!!
        movieDao = db

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

        return binding.root
    }


    private fun setCardViewClickListener(cardView: View, filmId: String) {
        cardView.tag = filmId
        cardView.setOnClickListener {
            openMovieDetails(filmId)
        }
    }

    private fun openMovieDetails(filmId: String) {
        val intent = Intent(requireContext(), DetailsMovie::class.java)
        intent.putExtra("filmId", filmId)
        requireActivity().startActivity(intent)
    }

    private fun fetchDataFromFirestore(){

        val firestoreMovie = firebase.collection("movieadmin")
        firestoreMovie.get().addOnSuccessListener { documents ->
            val movieLocal = mutableListOf<Movie>()
            for (document in documents) {
                val movie = document.toObject<Movie>()
                movieLocal.add(movie)
            }

            CoroutineScope(Dispatchers.IO).launch {
                movieDao.deleteAllFilm()
                for (movie in movieLocal) {
                    movieDao.insertMovie(movie)
                }

                withContext(Dispatchers.Main) {
                    Log.d("FirebaseToLocal", "Penyalinan data selesai")
                }
            }
        }.addOnFailureListener{ exception->
                Log.e("Firestore", "Error getting documents: $exception")
            }
        }
    private fun getLoggedInUserEmail(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.email ?: "user@example.com"
    }

}




