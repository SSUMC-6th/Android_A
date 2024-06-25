package com.example.umc_6th.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_6th.R

class LockerMusicFileFragment : Fragment() {
    // 여기에 Fragment의 구현 내용을 작성합니다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 여기에서 Fragment의 레이아웃을 인플레이트합니다.
        return inflater.inflate(R.layout.fragment_locker_music_file, container, false)
    }

    // 필요한 경우 다른 Fragment 생명주기 메소드를 오버라이드합니다.
}