package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.uas.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil nilai email dari data login (menggunakan Firebase Authentication sebagai contoh)
        val userEmail = getLoggedInUserEmail()

        // Set nilai email ke TextView
        binding.yourUsn.text = userEmail

        // Set listener untuk membuka DetailsMovie activity saat CardView diklik
        setCardViewClickListener(binding.film1, "film1")
        setCardViewClickListener(binding.film2, "film2")
        setCardViewClickListener(binding.film3, "film3")
        setCardViewClickListener(binding.film4, "film4")
        setCardViewClickListener(binding.film5, "film5")
        setCardViewClickListener(binding.film6, "film6")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
