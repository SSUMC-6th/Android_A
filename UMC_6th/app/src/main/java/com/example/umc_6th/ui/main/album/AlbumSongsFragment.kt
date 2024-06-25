package com.example.umc_6th.ui.main.album

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_6th.R
import com.example.umc_6th.databinding.FragmentAlbumSongsBinding
import com.example.umc_6th.ui.song.SongActivity

class AlbumSongsFragment : Fragment(R.layout.fragment_album_songs) {
    private var _binding: FragmentAlbumSongsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 취향 mix 이미지 변경
        binding.imgSongMixOffTag.setOnClickListener(){
            binding.imgSongMixOffTag.visibility=View.GONE
            binding.imgSongMixOnTag.visibility=View.VISIBLE
        }
        binding.imgSongMixOnTag.setOnClickListener(){
            binding.imgSongMixOnTag.visibility=View.GONE
            binding.imgSongMixOffTag.visibility=View.VISIBLE
        }

        binding.imgSongPlay01.setOnClickListener(){
            goToSongActivity(binding.txSongTitle01.text.toString(), binding.txSongArtist01.text.toString())
        }
    }

    private fun goToSongActivity(songTitle: String, songArtist: String) {
        val intent = Intent(activity, SongActivity::class.java).apply {
            putExtra("songTitle", songTitle)
            putExtra("songArtist", songArtist)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수를 방지하기 위해 binding 객체를 null로 설정
        _binding = null
    }
}