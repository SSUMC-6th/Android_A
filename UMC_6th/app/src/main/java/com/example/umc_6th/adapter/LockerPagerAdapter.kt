package com.example.umc_6th.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_6th.LockerMusicFileFragment
import com.example.umc_6th.LockerSavedSongFragment

class LockerPagerAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LockerSavedSongFragment()
            else -> LockerMusicFileFragment()
        }
    }
}