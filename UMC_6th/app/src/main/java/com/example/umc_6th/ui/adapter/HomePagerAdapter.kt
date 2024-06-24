package com.example.umc_6th.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_6th.ui.main.home.FragmentHomeMainBanner
import com.example.umc_6th.ui.main.home.FragmentHomeMainBanner2

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
