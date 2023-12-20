package com.example.uas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.uas.FragmentLogin
import com.example.uas.FragmentRegister
import com.example.uas.HomeUser
import com.example.uas.CrudMovie

class TabAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragmentLogin()
            1 -> FragmentRegister()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Login"
            1 -> "Register"
            else -> null
        }
    }
}


//
//import android.content.SharedPreferences
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentPagerAdapter
//
//class TabAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//    private lateinit var sharedPreferences: SharedPreferences
//
//    override fun onCreate(savedInstanceState: Bundle?){
//        sharedPreferences = getSharedPreferences
//    }
//    override fun getCount(): Int {
//        return 2
//    }
//
//    override fun getItem(position: Int): Fragment {
//        return when (position) {
//            0 -> FragmentLogin()
//            1 -> FragmentRegister()
//            else -> throw IllegalArgumentException("Invalid tab position")
//        }
//    }
//    override fun getPageTitle(position: Int): CharSequence?{
//        return when (position) {
//            0 -> "Login"
//            1 -> "Register"
//            else -> null
//        }
//    }
//}