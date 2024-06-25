package com.example.umc_6th.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_6th.ui.main.locker.LockerMusicFileFragment
import com.example.umc_6th.ui.main.locker.LockerSavedAlbumFragment
import com.example.umc_6th.ui.main.locker.LockerSavedSongFragment

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