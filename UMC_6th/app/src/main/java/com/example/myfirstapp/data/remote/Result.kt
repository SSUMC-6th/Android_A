package com.example.myfirstapp.data.remote

import com.google.gson.annotations.SerializedName

data class Result (
    @SerializedName("userIdx") var userIdx : Int,
    @SerializedName("jwt") var jwt : String
)
