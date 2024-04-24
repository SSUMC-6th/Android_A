package com.example.myfirstapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myfirstapp.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {

    lateinit var binding : FragmentAlbumBinding

    private  val information = arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container,false)

        //HomeFragment를 AlbumFragment로 전환할때 전달된 데이터 받는 곳(bundle을 이용해 전달 가능)
        binding.albumTitleTv.text = arguments?.getString("title")
        binding.albumSingerTv.text = arguments?.getString("singer")

        binding.albumBackIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_container,HomeFragment()).commitAllowingStateLoss()
        }

        val albumVpAdapter = AlbumVpAdapter(this)
        binding.albumContentVp.adapter = albumVpAdapter
        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
            tab ,position ->
            tab.text =information[position]
        }.attach()

        return binding.root
    }
}