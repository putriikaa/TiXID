package com.example.uas

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityFragmentLoginBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        binding.loginButton.setOnClickListener {
            val enteredEmail = binding.email.text.toString()
            val enteredPassword = binding.password.text.toString()

            auth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(requireActivity()) { task ->
                    val currentUser = auth.currentUser
                    if (task.isSuccessful && currentUser != null) {
                        if (currentUser.email == "adminnanda@gmail.com") {
                            // Start admin activity
                            startActivity(Intent(requireActivity(), CrudMovie::class.java))
                        } else {
                            // Start regular activity
                            startActivity(Intent(requireActivity(), HomeUser::class.java))
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        return binding.root
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}
