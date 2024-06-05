package com.example.umc_6th

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_6th.databinding.FragmentLookBinding

class LookFragment : Fragment() {

    lateinit var binding: FragmentLookBinding
    private lateinit var songDB: SongDatabase

    private lateinit var chartBtn : Button
    private lateinit var videoBtn : Button
    private lateinit var genreBtn : Button
    private lateinit var situationBtn : Button
    private lateinit var audioBtn : Button
    private lateinit var atmosphereBtn : Button

    private lateinit var buttonList: List<Button>

    private lateinit var chartTv : TextView
    private lateinit var videoTv : TextView
    private lateinit var genreTv : TextView
    private lateinit var situationTv : TextView
    private lateinit var audioTv : TextView
    private lateinit var atmosphereTv : TextView

    private lateinit var textList: List<TextView>

    lateinit var scrollView : ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        // 스크롤 뷰 초기화
        scrollView = binding.lookSv

        // 버튼 초기화
        chartBtn = binding.lookChartBtn
        videoBtn =  binding.lookVideoBtn
        genreBtn =  binding.lookGenreBtn
        situationBtn =  binding.lookSituationBtn
        audioBtn =  binding.lookAudioBtn
        atmosphereBtn =  binding.lookAtmostphereBtn

        buttonList = listOf(chartBtn, videoBtn, genreBtn, situationBtn, audioBtn, atmosphereBtn)

        // 텍스트 초기화
        chartTv = binding.lookChartTv
        videoTv = binding.lookVideoTv
        genreTv = binding.lookGenreTv
        situationTv = binding.lookSituationTv
        audioTv = binding.lookAudioTv
        atmosphereTv = binding.lookAtmostphereTv

        textList = listOf(chartTv, videoTv, genreTv, situationTv, audioTv, atmosphereTv)

        setButtonClickListeners()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        val recyclerView = binding.lookChartSongRv
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val lookAlbumRVAdapter = LockerAlbumRVAdapter()

        binding.lookChartSongRv.adapter = lookAlbumRVAdapter
        lookAlbumRVAdapter.addSongs(songDB.songDao().getSongs() as ArrayList<Song>)
    }

    private fun setButtonClickListeners() {
        for (i in buttonList.indices) {
            val button = buttonList[i]

            button.setOnClickListener {
                initButton(i)
            }
        }
    }

    private fun initButton(idx : Int) {
        for(presentBtn : Button in buttonList) {
            if(presentBtn == buttonList[idx]) {
                presentBtn.setBackgroundResource(R.drawable.selected_button)
            } else {
                presentBtn.setBackgroundResource(R.drawable.not_selected_button)
            }
        }
        scrollView.smoothScrollTo(0, textList[idx].top)
    }
}