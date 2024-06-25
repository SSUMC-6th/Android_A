package com.example.myfirstapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstapp.ui.main.locker.MusicFileFragment
import com.example.myfirstapp.ui.main.locker.SavedAlbumFragment
import com.example.myfirstapp.ui.main.locker.SavedSongFragment

class LockerVPAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            else -> SavedAlbumFragment()
        }
    }


}