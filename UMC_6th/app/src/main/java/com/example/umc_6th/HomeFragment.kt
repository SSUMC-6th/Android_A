package com.example.umc_6th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.umc_6th.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    // 여기에 Fragment의 구현 내용을 작성합니다.
    private var _binding: FragmentHomeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val imgSecondAlbum1 = view.findViewById<ImageView>(R.id.imgSecondAlbum1)
        imgSecondAlbum1.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, AlbumFragment())
            transaction.addToBackStack(null)  // 백 스택에 추가
            transaction.commit()
        }

        return view
        }
    }
    // 필요한 경우 다른 Fragment 생명주기 메소드를 오버라이드합니다.
