package com.example.myfirstapp.ui.main.look

import com.example.myfirstapp.data.remote.FloChartResult

interface LookView {
    fun onGetSongLoading()
    fun onGetSongSuccess(code: Int, result: FloChartResult)
    fun onGetSongFailure(code: Int, message: String)
}