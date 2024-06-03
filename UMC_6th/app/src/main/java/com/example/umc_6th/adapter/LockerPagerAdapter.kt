package com.example.umc_6th.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_6th.LockerMusicFileFragment
import com.example.umc_6th.LockerSavedAlbumFragment
import com.example.umc_6th.LockerSavedSongFragment

class LockerPagerAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LockerSavedSongFragment()
            1 -> LockerMusicFileFragment()
            else -> LockerSavedAlbumFragment()
        }
    }
}