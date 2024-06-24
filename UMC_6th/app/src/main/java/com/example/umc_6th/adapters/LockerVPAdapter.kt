package com.example.umc_6th.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_6th.ui.SavedAlbumFragment
import com.example.umc_6th.ui.SavedSongFragment
import com.example.umc_6th.music.MusicFileFragment

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