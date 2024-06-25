package com.example.umc_6th.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_6th.fragment.MusicFileFragment
import com.example.umc_6th.fragment.SavedAlbumFragment
import com.example.umc_6th.fragment.SavedSongFragment

class LockerVPAdapter(fragment: Fragment) :FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            else -> SavedAlbumFragment()
        }
    }
}