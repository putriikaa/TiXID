package com.example.uas

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uas.databinding.ActivityFragmentRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class FragmentRegister : Fragment() {
    private lateinit var binding: ActivityFragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPrefrences: SharedPreferences

    private val TAG = "RegisterFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityFragmentRegisterBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        sharedPrefrences = requireActivity().getSharedPreferences("user_ana", Context.MODE_PRIVATE)
        val editorrrrrr = sharedPrefrences.edit()

        binding.ButtonRegister.setOnClickListener {
            Toast.makeText(requireActivity(),"aowkaokowa0",Toast.LENGTH_SHORT).show()
            val enteredUsername = binding.username.text.toString()
            val enteredEmail = binding.email.text.toString()
            val enteredPassword = binding.password.text.toString()

            auth.createUserWithEmailAndPassword(enteredEmail,enteredPassword)
                .addOnCompleteListener(requireActivity()) {task ->
                    if (task.isSuccessful){
                        Toast.makeText(requireActivity(),"BISAAA",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireActivity(),"GBSAAAA",Toast.LENGTH_SHORT).show()

                    }
                }

        }
        return binding.root
    }
}
