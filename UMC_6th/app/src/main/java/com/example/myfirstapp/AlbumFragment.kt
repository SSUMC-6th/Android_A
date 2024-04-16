package com.example.myfirstapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myfirstapp.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

    lateinit var binding : FragmentAlbumBinding

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
        binding.albumSongLalacCl.setOnClickListener{
            Toast.makeText(activity,"LILAC",Toast.LENGTH_SHORT).show()
        }
        binding.albumSongFluCl.setOnClickListener{
            Toast.makeText(activity,"FLU",Toast.LENGTH_SHORT).show()
        }
        binding.albumSongCoinCl.setOnClickListener{
            Toast.makeText(activity,"Coin",Toast.LENGTH_SHORT).show()
        }
        binding.albumSongSpringhelloCl.setOnClickListener{
            Toast.makeText(activity,"봄 안녕 봄",Toast.LENGTH_SHORT).show()
        }
        binding.albumSongCelebrityCl.setOnClickListener{
            Toast.makeText(activity,"Celebrity",Toast.LENGTH_SHORT).show()
        }
        binding.albumSongSingCl.setOnClickListener{
            Toast.makeText(activity,"돌림노래 (Feat.DEAN)",Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}