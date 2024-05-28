package com.example.umc_6th


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.umc_6th.adapter.HomePagerAdapter
import com.example.umc_6th.adapter.ViewPagerAdapter
import com.example.umc_6th.databinding.FragmentHomeBinding
import me.relex.circleindicator.CircleIndicator3
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_6th.adapter.AlbumRecyclerAdapter
import com.google.gson.Gson


class HomeFragment : Fragment() {
    // 여기에 Fragment의 구현 내용을 작성합니다.
    private lateinit var viewModel: SharedViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var albumDatas = ArrayList<Album>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        albumDatas.apply {
            add(Album(0, "Love wins all", "아이유 (IU)", R.drawable.img_album_lovewinsall))
            add(Album(1, "해야 (HEYA)", "IVE", R.drawable.img_album_heya))
            add(Album(2, "Supernova", "에스파 (aespa)", R.drawable.img_album_supernova))
            add(Album(3, "Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album(4, "Drama", "에스파 (aespa)", R.drawable.img_album_drama))
            add(Album(5, "Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
        }
        val albumRecyclerAdapter = AlbumRecyclerAdapter(albumDatas)
        binding.homeTodayMusicAlbum.adapter = albumRecyclerAdapter
        binding.homeTodayMusicAlbum.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        albumRecyclerAdapter.setItemPlayClickListener(object : AlbumRecyclerAdapter.OnItemPlayClickListener{
            override fun onItemPlayClick(album: Album) {
                album.title?.let { album.artist?.let { it1 -> viewModel.selectItem(it, it1) } }
            }
        })
        albumRecyclerAdapter.setItemClickListener(object : AlbumRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(album : Album) {
                changeToAlbumFragment(album)
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, AlbumFragment())
                    .commitAllowingStateLoss()
            }
        })
        return binding.root

    }

    private fun changeToAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumToJson = gson.toJson(album)
                    putString("album", albumToJson)
                }
            })
            .commitAllowingStateLoss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = ViewPagerAdapter(this)
        binding.homeViewPager.adapter = adapter

        val mainAdapter = HomePagerAdapter.HomeMainViewPagerAdapter(this)
        val viewpager: ViewPager2 = binding.homeMainViewPager
        viewpager.adapter = mainAdapter

        val indicator: CircleIndicator3 = binding.indicator
        indicator.setViewPager(viewpager)

        // Handler 및 Runnable 설정
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val itemCount = adapter.itemCount
                val nextItem = (binding.homeMainViewPager.currentItem + 1) % itemCount
                binding.homeMainViewPager.currentItem = nextItem
                handler.postDelayed(this, 3000) // 3초 후에 다음 페이지로 넘어감
            }
        }
        startAutoSlide()


    }

    class SharedViewModel : ViewModel() {
        val selectedTitle = MutableLiveData<String>()
        val selectedArtist = MutableLiveData<String>()
        fun selectItem(title: String, artist: String) {
            selectedTitle.value = title
            selectedArtist.value = artist
        }
    }


    private fun startAutoSlide() {
        handler.postDelayed(runnable, 3000) // 3초에 한번씩 페이지 이동
    }

    private fun stopAutoSlide() {
        handler.removeCallbacks(runnable) // 자동 슬라이드 중지
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoSlide() // 뷰가 사라질 때 자동 슬라이드 중지
        _binding = null
    }



    class FragmentHomeBanner : Fragment(R.layout.fragment_home_banner1) {
        // 필요한 경우 여기에 로직 추가
    }
}
