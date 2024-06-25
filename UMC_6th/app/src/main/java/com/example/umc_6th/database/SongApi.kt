package com.example.umc_6th.database

import com.example.umc_6th.models.SongResponse
import retrofit2.http.GET
import retrofit2.Call

interface SongApi {
    @GET("/songs")
    fun getSongs(): Call<SongResponse>
}