package com.example.uas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uas.databinding.ActivityFragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class FragmentLogin : Fragment() {

    private lateinit var binding: ActivityFragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var prefManager: PrefManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityFragmentLoginBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        prefManager = PrefManager.getInstance(requireContext())

        binding.loginButton.setOnClickListener {
            val enteredEmail = binding.email.text.toString()
            val enteredPassword = binding.password.text.toString()

            auth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(requireActivity()) { task ->
                    val currentUser = auth.currentUser
                    if (task.isSuccessful && currentUser != null) {
                        currentUser.email?.let { itt -> prefManager.saveUsername(itt) }
                        prefManager.setLoggedIn(true)
                        val userType = if (currentUser.email == "adminnanda@gmail.com") {
                            "admin"
                        } else {
                            "user"
                        }


                        // Start admin activity atau regular activity sesuai dengan userType
                        val intentClass = when (userType) {
                            "user" -> Intent(requireActivity(), HomeUser::class.java)
                            "admin" -> Intent(requireActivity(), CrudMovie::class.java)
                            else -> null
                        }

                        if (intentClass != null) {
                            startActivity(intentClass)
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
            return binding.root
    }
}
