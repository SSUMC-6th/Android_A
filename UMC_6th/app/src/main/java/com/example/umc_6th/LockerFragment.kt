package com.example.umc_6th

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_6th.adapter.LockerPagerAdapter
import com.example.umc_6th.databinding.FragmentHomeBinding
import com.example.umc_6th.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!
    private val information = arrayListOf("저장한곡", "음악파일")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerPagerAdapter(this)
        binding.vpLocker.adapter = lockerAdapter
        TabLayoutMediator(binding.tbLocker, binding.vpLocker) { tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}