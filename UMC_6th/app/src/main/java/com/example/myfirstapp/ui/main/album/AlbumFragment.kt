package com.example.myfirstapp.ui.main.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myfirstapp.ui.adapter.AlbumVpAdapter
import com.example.myfirstapp.ui.main.home.HomeFragment
import com.example.myfirstapp.ui.main.MainActivity
import com.example.myfirstapp.R
import com.example.myfirstapp.data.local.SongDatabase
import com.example.myfirstapp.data.entities.Album
import com.example.myfirstapp.data.entities.Like
import com.example.myfirstapp.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()

    private var isLiked : Boolean = false

    private  val information = arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container,false)

        val albumToJson = arguments?.getString("album")
        val album = gson.fromJson(albumToJson, Album::class.java)
        isLiked = isLikedAlbum(album.id)
        setInit(album)
        setOnClickListener(album)

        binding.albumBackIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(
                R.id.main_container,
                HomeFragment()
            ).commitAllowingStateLoss()
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
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerTv.text = album.singer.toString()
    }

    private fun getJwt() : Int {
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf.getInt("jwt", 0)
    }

    private fun likeAlbum(userId : Int, albumId : Int) {
        val songDB = SongDatabase.getInstance(requireActivity())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId : Int) : Boolean {
        val songDB = SongDatabase.getInstance(requireActivity())!!
        val userId = getJwt()

        val likeId : Int? = songDB.albumDao().isLikedAlbum(userId, albumId)
        return likeId != null
    }

    private fun disLikeAlbum(albumId : Int) {
        val songDB = SongDatabase.getInstance(requireActivity())!!
        val userId = getJwt()

        songDB.albumDao().dislikedAlbum(userId, albumId)
    }

    private fun setOnClickListener(album : Album) {
        val userId = getJwt()
        binding.albumLikeIv.setOnClickListener {
            if(isLiked) {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikeAlbum(album.id)
            }

            else {
                binding.albumLikeIv.setImageResource((R.drawable.ic_my_like_on))
                likeAlbum(userId, album.id)
            }
        }

    }
}