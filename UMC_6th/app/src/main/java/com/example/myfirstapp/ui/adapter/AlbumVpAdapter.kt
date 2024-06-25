package com.example.myfirstapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfirstapp.ui.main.album.DetailFragment
import com.example.myfirstapp.ui.main.album.SongFragment
import com.example.myfirstapp.ui.main.album.VideoFragment

class AlbumVpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }

}