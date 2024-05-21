package com.example.umc_6th

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.umc_6th.databinding.FragmentHomeBinding
import com.google.gson.Gson
import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment(), AlbumRVAdapter.CommunicationInterface {

    lateinit var binding: FragmentHomeBinding

    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())
    private var albumDatas = ArrayList<Album>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun sendData(album: Album) {
        if (activity is MainActivity) {
            val activity = activity as MainActivity
            activity.updateMainPlayerCl(album)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

//        binding.homeAlbumImg01Iv1.setOnClickListener{
//            setFragmentResult("TitleInfo", bundleOf("title" to binding.titleLilac.text.toString()))
//            setFragmentResult("SingerInfo", bundleOf("singer" to binding.singerIu.text.toString()))
//            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm,AlbumFragment()).commitAllowingStateLoss()
//        }

        albumDatas.apply{
            add(Album("Butter","방탄소년단(BTS)",R.drawable.img_album_exp))
            add(Album("Lilac","아이유(IU)",R.drawable.img_album_exp2))
            add(Album("Next Level","에스파(AESPA)",R.drawable.img_album_exp3))
            add(Album("Boy with Luv","방탄소년단(BTS)",R.drawable.img_album_exp4))
            add(Album("BBoom BBoom","모모랜드(MOMOLAND)",R.drawable.img_album_exp5))
            add(Album("Weekend","태연(Tae Yeon)",R.drawable.img_album_exp6))
        }

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }
            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
            override fun onPlayAlbum(album: Album) {
                sendData(album)
            }
        })

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.homeBannerIndicator.setViewPager(binding.homeBannerVp)

        autoSlide(bannerAdapter)

        val pannelVPAdapter = PannelVPAdapter(this)
        pannelVPAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        pannelVPAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))

        binding.homePannelBackgroundVp.adapter = pannelVPAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.homePannelIndicator.setViewPager(binding.homePannelBackgroundVp)

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    private fun autoSlide(adapter: BannerVPAdapter) {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    val nextItem = binding.homeBannerVp.currentItem + 1
                    if (nextItem < adapter.itemCount) {
                        binding.homeBannerVp.currentItem = nextItem
                    } else {
                        binding.homeBannerVp.currentItem = 0 // 순환
                    }
                }
            }
        }, 3000, 3000)
    }

}