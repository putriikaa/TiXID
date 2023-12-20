package com.example.uas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerViewItem : RecyclerView
    private lateinit var itemAdapter : UserAdapter
    private lateinit var itemList : ArrayList<Movie>

    private val firebase = FirebaseFirestore.getInstance()
    private val movieadminCollectionRef = firebase.collection("movieadmin")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Ambil nilai email dari data login (menggunakan Firebase Authentication sebagai contoh)
        val userEmail = getLoggedInUserEmail()

        // Set nilai email ke TextView
        binding.yourUsn.text = userEmail

        recyclerViewItem = binding.recyclerViewMovies
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(requireActivity())

        itemList = arrayListOf()
        itemAdapter = UserAdapter(itemList, movieadminCollectionRef)
        recyclerViewItem.adapter = itemAdapter

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
        // Buat Intent untuk membuka DetailsMovie Activity
        val intent = Intent(requireContext(), DetailsMovie::class.java)

        // Kirim data tambahan (jika diperlukan)
        intent.putExtra("filmId", filmId)

        // Mulai DetailsMovie Activity
        requireActivity().startActivity(intent)
    }


    private fun getLoggedInUserEmail(): String {
        // Gantilah dengan logika yang sesuai untuk mendapatkan nilai email setelah login
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.email ?: "user@example.com"
    }

}
