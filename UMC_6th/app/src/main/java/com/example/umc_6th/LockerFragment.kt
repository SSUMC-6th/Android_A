package com.example.umc_6th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.umc_6th.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private var information = arrayListOf("저장한곡", "음악파일", "저장앨범")
    val bottomSheetFragment = BottomSheetFragment()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerContentTb,binding.lockerContentVp){
            tab,position ->
            tab.text = information[position]
        }.attach()

        binding.lockerSelectAllImgIv.setOnClickListener {
            bottomSheetFragment.show(requireFragmentManager(),"BottomSheetDialog")
        }

        binding.lockerSelectAllTv.setOnClickListener{
            bottomSheetFragment.show(requireFragmentManager(),"BottomSheetDialog")
        }

        return binding.root
    }
}