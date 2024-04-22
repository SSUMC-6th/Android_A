package com.example.umc_6th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_6th.databinding.FragmentHomeBinding
import androidx.viewpager2.adapter.FragmentStateAdapter


class HomeFragment : Fragment(R.layout.fragment_home) {
    // 여기에 Fragment의 구현 내용을 작성합니다.
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
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
    }

    class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        private val fragments = arrayOf(FragmentHomeBanner())
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }


    class FragmentHomeBanner : Fragment(R.layout.fragment_home_banner1) {
        // 필요한 경우 여기에 로직 추가
    }
}
