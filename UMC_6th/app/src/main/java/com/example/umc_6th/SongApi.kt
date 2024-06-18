package com.example.umc_6th

import retrofit2.http.GET
import retrofit2.Call

interface SongApi {
    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}