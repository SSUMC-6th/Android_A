package com.example.myfirstapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myfirstapp.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()

    private  val information = arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container,false)

        val albumToJson = arguments?.getString("album")
        val album = gson.fromJson(albumToJson, Album::class.java)
        setInit(album)

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

    private fun setInit(album : Album) {
        binding.albumAlbumIv.setImageResource(album.coverImage!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerTv.text = album.singer.toString()
    }
}