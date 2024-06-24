package com.example.umc_6th.data.remote.album

import retrofit2.Call
import retrofit2.http.GET

interface AlbumApi {
    @GET("/albums")
    fun getAlbums(): Call<AlbumResponse>
}
