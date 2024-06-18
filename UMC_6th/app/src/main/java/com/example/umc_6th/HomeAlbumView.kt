package com.example.umc_6th

interface HomeAlbumView {
    fun onGetAlbumLoading()
    fun onGetAlbumSuccess(code: Int, result: TodayAlbumResult)
    fun onGetAlbumFailure(code: Int, message: String)
}