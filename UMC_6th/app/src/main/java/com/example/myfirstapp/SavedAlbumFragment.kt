package com.example.myfirstapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstapp.databinding.FragmentDetailBinding
import com.example.myfirstapp.databinding.FragmentMusicfileBinding
import com.example.myfirstapp.databinding.FragmentSavedalbumBinding

class SavedAlbumFragment : Fragment(){

    lateinit var binding: FragmentSavedalbumBinding
    lateinit var albumDB: SongDatabase
    private lateinit var albumRVAdapter: SavedAlbumRVAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedalbumBinding.inflate(inflater,container,false)

        albumDB = SongDatabase.getInstance(requireContext())!!
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        albumRVAdapter = SavedAlbumRVAdapter()
        //리스너 객체 생성 및 전달

        albumRVAdapter.setMyItemClickListener(object : SavedAlbumRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                albumDB.albumDao().getLikedAlbums(getJwt())
                val album = albumDB.albumDao().getAlbum(songId)
                album.isLike = false
                albumDB.albumDao().updateAlbum(album)
                refreshRecyclerView()
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = albumRVAdapter

        albumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbums(getJwt()) as ArrayList)
        refreshRecyclerView()
    }

    private fun refreshRecyclerView() {
        albumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbums(getJwt()) as ArrayList<Album>)
    }
    private fun getJwt() : Int {
        val spf = activity?.getSharedPreferences("auth" , AppCompatActivity.MODE_PRIVATE)
        val jwt = spf!!.getInt("jwt", 0)
        Log.d("MAIN_ACT/GET_JWT", "jwt_token: $jwt")

        return jwt
    }
}