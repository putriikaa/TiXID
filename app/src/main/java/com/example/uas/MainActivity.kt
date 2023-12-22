package com.example.uas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uas.databinding.ActivityMainBinding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = TabAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        prefManager = PrefManager.getInstance(this@MainActivity)

//         Call navigateToCorrectActivity after setting up the adapter
        Log.d("sharepref", prefManager.getUsername())

        if (prefManager.isLoggedIn()){
            if (prefManager.getUsername() == "adminnanda@gmail.com"){
                startActivity(Intent(this@MainActivity, CrudMovie::class.java))
            }else{
                startActivity(Intent(this@MainActivity, HomeUser::class.java))
            }
        }

    }

}
