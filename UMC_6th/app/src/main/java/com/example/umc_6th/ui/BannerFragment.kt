package com.example.umc_6th.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.umc_6th.databinding.FragmentBannerBinding

class BannerFragment(val imgRes : Int) : Fragment() {

    lateinit var binding : FragmentBannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)
        binding.bannerImageIv.setImageResource(imgRes)
        return binding.root
    }
}