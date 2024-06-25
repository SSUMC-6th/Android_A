package com.example.myfirstapp

import com.google.gson.annotations.SerializedName

data class Result (
    @SerializedName("userIdx") var userIdx : Int,
    @SerializedName("jwt") var jwt : String
)
