package com.example.uas


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
    override fun getPageTitle(position: Int): CharSequence?{
        return when (position) {
            0 -> "Login"
            1 -> "Register"
            else -> null
        }
    }
}