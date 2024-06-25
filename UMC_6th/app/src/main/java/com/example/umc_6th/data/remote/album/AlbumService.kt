package com.example.umc_6th.data.remote.album

import android.util.Log
import com.example.umc_6th.ui.main.home.HomeAlbumView
import com.example.umc_6th.data.remote.auth.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumService {
    private lateinit var homeAlbumView: HomeAlbumView

    fun setHomeAlbumView(homeAlbumView: HomeAlbumView) {
        this.homeAlbumView = homeAlbumView
    }

    fun getAlbum() {
        homeAlbumView.onGetAlbumLoading()

        RetrofitInstance.albumApi.getAlbums().enqueue(object : Callback<AlbumResponse> {
            override fun onResponse(call: Call<AlbumResponse>, response: Response<AlbumResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val albumResponse: AlbumResponse = response.body()!!

                    Log.d("ALBUM-RESPONSE", albumResponse.toString())

                    when (val code = albumResponse.code) {
                        1000 -> {
                            homeAlbumView.onGetAlbumSuccess(code, albumResponse.result!!)
                        }

                        else -> homeAlbumView.onGetAlbumFailure(code, albumResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<AlbumResponse>, t: Throwable) {
                homeAlbumView.onGetAlbumFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}