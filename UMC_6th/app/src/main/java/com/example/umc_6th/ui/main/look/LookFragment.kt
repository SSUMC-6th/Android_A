package com.example.umc_6th.ui.main.look

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_6th.ui.adapter.SongRecyclerViewAdapter
import com.example.umc_6th.data.local.SongDatabase
import com.example.umc_6th.data.remote.song.FloChartResult
import com.example.umc_6th.data.remote.song.SongService
import com.example.umc_6th.databinding.FragmentLookBinding

class LookFragment : Fragment(), LookView {
    private var _binding: FragmentLookBinding? = null
    private val binding get() = _binding!!
    lateinit var songDB: SongDatabase
    private lateinit var floCharAdapter: SongRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLookBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getSongs()
    }

    private fun initRecyclerView(result: FloChartResult) {
        floCharAdapter = SongRecyclerViewAdapter(requireContext(), result)

        binding.rvLookFloChart.adapter = floCharAdapter
    }

    private fun getSongs() {
        val songService = SongService()
        songService.setLookView(this)

        songService.getSongs()

    }

    override fun onGetSongLoading() {
        binding.lookLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetSongSuccess(code: Int, result: FloChartResult) {
        binding.lookLoadingPb.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetSongFailure(code: Int, message: String) {
        binding.lookLoadingPb.visibility = View.GONE
        Log.d("LOOK-FRAG/SONG-RESPONSE", message)
    }
}