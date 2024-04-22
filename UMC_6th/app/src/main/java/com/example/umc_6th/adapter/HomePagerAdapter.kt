package com.example.umc_6th.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_6th.FragmentHomeMainBanner
import com.example.umc_6th.FragmentHomeMainBanner2

class HomePagerAdapter {
    class HomeMainViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        private val fragments = listOf(
            FragmentHomeMainBanner(),
            FragmentHomeMainBanner2()
        )

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}
