package com.example.myfirstapp.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfirstapp.databinding.FragmentDetailBinding
import com.example.myfirstapp.databinding.FragmentMusicfileBinding

class MusicFileFragment : Fragment(){

    lateinit var binding: FragmentMusicfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicfileBinding.inflate(inflater,container,false)
        return binding.root
    }
}