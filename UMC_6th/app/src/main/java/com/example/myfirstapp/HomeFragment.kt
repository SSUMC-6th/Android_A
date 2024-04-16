package com.example.myfirstapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfirstapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.homeTodayAlbum1Iv.setOnClickListener {
            //HomeFragment를 AlbumFragment로 전환할때 데이터도 이동(bundle을 이용해 전달 가능)
            val bundle = Bundle()
            bundle.putString("title", binding.homeTodayAlbumTitleTv.text.toString())
            bundle.putString("singer", binding.homeTodaySingerTv.text.toString())

            val albumFragment = AlbumFragment()
            albumFragment.arguments = bundle

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, albumFragment).commitAllowingStateLoss()
        }
        return binding.root
    }
}