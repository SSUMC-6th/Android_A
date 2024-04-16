package com.example.umc_6th

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class AlbumFragment: Fragment() {
    // 여기에 Fragment의 구현 내용을 작성합니다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album, container, false)

        // 뒤로가기 이미지 뷰에 클릭 리스너 설정
        val albumBackIv = view.findViewById<ImageView>(R.id.album_back_iv)
        albumBackIv.setOnClickListener {
            // 이전 프래그먼트로 돌아가기
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view

    }
    // 필요한 경우 다른 Fragment 생명주기 메소드를 오버라이드합니다.
}