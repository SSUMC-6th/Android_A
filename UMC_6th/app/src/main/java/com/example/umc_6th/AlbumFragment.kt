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

    lateinit var binding : FragmentAlbumBinding

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
            binding.albumMusicTitleTv.text = bundle.getString("title")
        }
        setFragmentResultListener("SingerInfo") { requestKey, bundle ->
            binding.albumSingerNameTv.text = bundle.getString("singer")
        }

        TabLayoutMediator(binding.albumContentTb,binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()


//        binding.songLalacLayout.setOnClickListener{
//            Toast.makeText(activity,"LILAC",Toast.LENGTH_SHORT).show()
//        }
//
//        binding.songFluLayout.setOnClickListener {
//            Toast.makeText(activity,"FLU", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.songCoinLayout.setOnClickListener {
//            Toast.makeText(activity,"Coin", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.songSpringLayout.setOnClickListener {
//            Toast.makeText(activity,"봄 안녕 봄", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.songCelebrityLayout.setOnClickListener {
//            Toast.makeText(activity,"Celebrity", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.songSingLayout.setOnClickListener {
//            Toast.makeText(activity,"돌림노래 (Feat. DEAN)", Toast.LENGTH_SHORT).show()
//        }
        return binding.root
    }
}