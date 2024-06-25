package com.example.umc_6th.network

import com.example.umc_6th.model.SongResponse
import retrofit2.Call
import retrofit2.http.GET

interface SongApi {
    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}