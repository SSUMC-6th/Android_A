package com.example.umc_6th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_6th.databinding.FragmentLockerMusicfileBinding

class MusicFileFragment : Fragment() {
    lateinit var binding:FragmentLockerMusicfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerMusicfileBinding.inflate(inflater,container,false)

        return binding.root
    }
}