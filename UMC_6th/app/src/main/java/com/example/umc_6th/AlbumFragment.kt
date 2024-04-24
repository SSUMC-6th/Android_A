package com.example.umc_6th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.umc_6th.databinding.FragmentAlbumBinding
import com.example.umc_6th.databinding.FragmentAlbumBinding.inflate
import com.example.umc_6th.databinding.FragmentSongBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding

    private  val information = arrayListOf("수록곡","상세정보","영상")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater,container,false)
        binding.albumBackIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().
            replace(R.id.main_frm,HomeFragment()).
            commitAllowingStateLoss()
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter


        setFragmentResultListener("TitleInfo") { requestKey, bundle ->
            binding.albumTitleTv.text = bundle.getString("title")
        }
        setFragmentResultListener("SingerInfo") { requestKey, bundle ->
            binding.albumSingerTv.text = bundle.getString("singer")
        }

        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
                tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }
}