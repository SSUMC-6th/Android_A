package com.example.umc_6th

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.umc_6th.databinding.FragmentAlbumBinding

class AlbumFragment: Fragment() {
    // 여기에 Fragment의 구현 내용을 작성합니다.

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 이미지 뷰에 클릭 리스너 설정
        binding.albumBackIv.setOnClickListener {
            // 이전 프래그먼트로 돌아가기
            requireActivity().supportFragmentManager.popBackStack()
        }

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

    fun goToSongActivity(songTitle: String, songArtist: String) {
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