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


class HomeFragment : Fragment() {
    // 여기에 Fragment의 구현 내용을 작성합니다.
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgSecondAlbum1.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, AlbumFragment())
            transaction.addToBackStack(null)  // 백 스택에 추가
            transaction.commit()
        }
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
