package com.example.uas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.uas.databinding.ActivityFragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FragmentRegister : Fragment() {
    private lateinit var binding: ActivityFragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var prefManager: PrefManager

    private val TAG = "RegisterFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityFragmentRegisterBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        prefManager = PrefManager.getInstance(requireContext())

        binding.ButtonRegister.setOnClickListener {
            Toast.makeText(requireActivity(), "aowkaokowa0", Toast.LENGTH_SHORT).show()
            val enteredUsername = binding.username.text.toString()
            val enteredEmail = binding.email.text.toString()
            val enteredPassword = binding.password.text.toString()

            auth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        prefManager.setLoggedIn(true)
                        Toast.makeText(requireActivity(), "BISAAA", Toast.LENGTH_SHORT).show()
                        sendNotification()
                        startActivity(Intent(requireContext(), HomeUser::class.java))
                    } else {
                        Toast.makeText(requireActivity(), "GBSAAAA", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Registration failed: ${task.exception?.message}")
                    }
                }
        }
        return binding.root
    }

    private fun sendNotification() {
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "Your_Channel_Id"
        val notificationId = 1 // You may use different notification IDs for different notifications

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Your_Channel_Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Widih! Selamat Bergabung")
            .setContentText("Selamat! Anda telah berhasil bergabung.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(notificationId, builder.build())
    }
}
