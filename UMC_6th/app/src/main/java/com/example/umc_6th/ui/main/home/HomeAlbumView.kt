package com.example.umc_6th.ui.main.home

import com.example.umc_6th.data.remote.album.TodayAlbumResult

interface HomeAlbumView {
    fun onGetAlbumLoading()
    fun onGetAlbumSuccess(code: Int, result: TodayAlbumResult)
    fun onGetAlbumFailure(code: Int, message: String)
}