package com.example.uas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uas.databinding.ActivityMainBinding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = TabAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        // Call navigateToCorrectActivity after setting up the adapter
        navigateToCorrectActivity()
    }

    private fun navigateToCorrectActivity() {
        val sharedPreferences = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userType = sharedPreferences.getString("userType", "guest")

        Toast.makeText(applicationContext, "User Type: $userType", Toast.LENGTH_SHORT).show()

        val intentClass = when (userType) {
            "user" -> HomeUser::class.java
            "admin" -> CrudMovie::class.java
            else -> null
        }

        if (intentClass != null) {
            val intent = Intent(applicationContext, intentClass)
            startActivity(intent)
        }
    }
}

//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        var binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        with(binding) {
//            viewPager.adapter = TabAdapter(supportFragmentManager)
//            tabLayout.setupWithViewPager(viewPager)
//        }
//    }
//}
