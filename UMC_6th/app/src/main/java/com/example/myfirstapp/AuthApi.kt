package com.example.myfirstapp


import retrofit2.Call
import retrofit2.http.*


interface AuthApi {
    @POST("/users")
    fun signUp(@Body user : User) : Call<BaseResponse>

    @POST("/users/login")
    fun login(@Body user : User) : Call<BaseResponse>
}