package com.example.umc_6th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.umc_6th.databinding.FragmentSongBinding


class SongFragment : Fragment(){

    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater,container,false)
        binding.albumSongMixOffIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.albumSongMixOnIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.albumSongLalacCl.setOnClickListener{
            Toast.makeText(activity,"LILAC", Toast.LENGTH_SHORT).show()
        }
        binding.albumSongFluCl.setOnClickListener{
            Toast.makeText(activity,"FLU", Toast.LENGTH_SHORT).show()
        }
        binding.albumSongCoinCl.setOnClickListener{
            Toast.makeText(activity,"Coin", Toast.LENGTH_SHORT).show()
        }
        binding.albumSongSpringhelloCl.setOnClickListener{
            Toast.makeText(activity,"봄 안녕 봄", Toast.LENGTH_SHORT).show()
        }
        binding.albumSongCelebrityCl.setOnClickListener{
            Toast.makeText(activity,"Celebrity", Toast.LENGTH_SHORT).show()
        }
        binding.albumSongSingCl.setOnClickListener{
            Toast.makeText(activity,"돌림노래 (Feat.DEAN)", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    fun setPlayerStatus(isPlaying : Boolean){
        if(isPlaying){ //내 취향 off
            binding.albumSongMixOffIv.visibility = View.VISIBLE
            binding.albumSongMixOnIv.visibility = View.GONE
        }
        else{ //내 취향 on
            binding.albumSongMixOffIv.visibility = View.GONE
            binding.albumSongMixOnIv.visibility = View.VISIBLE
        }
    }
}