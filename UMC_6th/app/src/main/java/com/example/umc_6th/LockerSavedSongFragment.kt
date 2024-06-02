package com.example.umc_6th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_6th.adapter.LockerAlbumRecyclerAdapter
import com.example.umc_6th.databinding.FragmentLockerSavedSongBinding
import com.google.gson.Gson

class LockerSavedSongFragment : Fragment(){
    private var songDatas = ArrayList<Song>()
    lateinit var binding : FragmentLockerSavedSongBinding
    lateinit var songDB: SongDatabase
    val lockerAlbumRecyclerAdapter = LockerAlbumRecyclerAdapter()

    // lockerFragment에서 bottom 버튼으로 인한 삭제
    private lateinit var sharedViewModel: LockerFragment.SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedSongBinding.inflate(inflater, container, false)
/*
        albumDatas.apply {
            add(Album(0, "Love wins all", "아이유 (IU)", R.drawable.img_album_lovewinsall))
            add(Album(1, "해야 (HEYA)", "IVE", R.drawable.img_album_heya))
            add(Album(2, "Supernova", "에스파 (aespa)", R.drawable.img_album_supernova))
            add(Album(3, "Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album(4, "Drama", "에스파 (aespa)", R.drawable.img_album_drama))
            add(Album(5, "Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
        }

 */
        binding.rvLockerSavedSong.adapter = lockerAlbumRecyclerAdapter
        binding.rvLockerSavedSong.layoutManager = LinearLayoutManager(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songDB = context?.let { SongDatabase.getInstance(it) }!!
        sharedViewModel = ViewModelProvider(requireActivity()).get(LockerFragment.SharedViewModel::class.java)
        sharedViewModel.dislikeAllEvent.observe(viewLifecycleOwner) { shouldDislikeAll ->
            if (shouldDislikeAll) {
                dislikeAllSongs()
                initRecyclerview()
            }
        }
    }

    private fun dislikeAllSongs() {
        // 모든 노래를 '싫어요' 상태로 업데이트하고 리사이클러뷰를 업데이트
        songDatas.forEach { song ->
            if (song.isLike) {
                song.isLike = false
                songDB.songDao().updateIsLikeById(song.isLike, song.id)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    override fun onResume() {
        super.onResume()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.rvLockerSavedSong.adapter = lockerAlbumRecyclerAdapter
        binding.rvLockerSavedSong.layoutManager = LinearLayoutManager(requireActivity())
//        //음악 가져오기
//        val songDB = SongDatabase.getInstance(requireContext())
//        if (songDB != null) {
//            songDatas = ArrayList(songDB.songDao().getLikedSongs(true))
//        }

        lockerAlbumRecyclerAdapter.setItemClickListener(object : LockerAlbumRecyclerAdapter.OnItemClickListener {
            override fun onRemoveAlbum(position: Int) {
                val song = songDatas[position]
                song.isLike = false
                if (songDB != null) {
                    songDB.songDao().update(song)
                }
                lockerAlbumRecyclerAdapter.removeItem(position)
            }

            override fun onItemClick(song: Song) {
                changeAlbumFragment(song)
            }
        })
        if (songDB != null) {
            lockerAlbumRecyclerAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
            val likedSongs = songDB.songDao().getLikedSongs(true)
            songDatas.addAll(likedSongs)
        }
        binding.rvLockerSavedSong.adapter = lockerAlbumRecyclerAdapter
    }

    private fun changeAlbumFragment(song: Song) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumToJson = gson.toJson(song)
                    putString("album", albumToJson)
                }
            })
            .commitAllowingStateLoss()
    }


    override fun onDestroy() {
        super.onDestroy()

    }
}