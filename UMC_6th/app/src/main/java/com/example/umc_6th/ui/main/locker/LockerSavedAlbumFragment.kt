package com.example.umc_6th.ui.main.locker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.umc_6th.ui.adapter.LockerSavedAlbumRecyclerAdapter
import com.example.umc_6th.databinding.FragmentLockerSavedAlbumBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_6th.data.local.SongDatabase

class LockerSavedAlbumFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedAlbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedAlbumBinding.inflate(inflater, container, false)

        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val lockerSavedAlbumRecyclerAdapter = LockerSavedAlbumRecyclerAdapter()
        //리스너 객체 생성 및 전달

        lockerSavedAlbumRecyclerAdapter.setMyItemClickListener(object : LockerSavedAlbumRecyclerAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                albumDB.albumDao().getLikedAlbums(getJwt())
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = lockerSavedAlbumRecyclerAdapter

        lockerSavedAlbumRecyclerAdapter.addAlbums(albumDB.albumDao().getLikedAlbums(getJwt()) as ArrayList)
    }

    private fun getJwt() : Int {
        val spf = activity?.getSharedPreferences("auth" , AppCompatActivity.MODE_PRIVATE)
        val jwt = spf!!.getInt("jwt", 0)
        Log.d("MAIN_ACT/GET_JWT", "jwt_token: $jwt")

        return jwt
    }


}
