package com.example.umc_6th.ui.main.look

import com.example.umc_6th.data.remote.song.FloChartResult

interface LookView {
    fun onGetSongLoading()
    fun onGetSongSuccess(code: Int, result: FloChartResult)
    fun onGetSongFailure(code: Int, message: String)
}