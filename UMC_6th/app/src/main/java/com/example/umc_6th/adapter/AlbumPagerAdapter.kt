package com.example.umc_6th.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_6th.AlbumDetailFragment
import com.example.umc_6th.AlbumSongsFragment
import com.example.umc_6th.AlbumVideoFragment


class AlbumPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments = listOf(
        AlbumSongsFragment(),
        AlbumDetailFragment(),
        AlbumVideoFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}