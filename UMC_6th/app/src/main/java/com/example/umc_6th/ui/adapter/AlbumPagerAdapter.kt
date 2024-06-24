package com.example.umc_6th.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_6th.ui.main.album.AlbumDetailFragment
import com.example.umc_6th.ui.main.album.AlbumSongsFragment
import com.example.umc_6th.ui.main.album.AlbumVideoFragment
import com.example.umc_6th.ui.main.home.HomeFragment


class AlbumPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments = listOf(
        AlbumSongsFragment(),
        AlbumDetailFragment(),
        AlbumVideoFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments = arrayOf(HomeFragment.FragmentHomeBanner())
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}