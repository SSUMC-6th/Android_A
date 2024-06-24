package com.example.umc_6th

import com.google.gson.annotations.SerializedName

data class AlbumResponse(    @SerializedName("isSuccess") val isSuccess: Boolean,
                             @SerializedName("code") val code: Int,
                             @SerializedName("message") val message: String,
                             @SerializedName("result") val result: TodayAlbumResult)


data class TodayAlbumResult(
    @SerializedName("albums") val albums: List<TodayAlbum>
)

data class TodayAlbum(
    @SerializedName("albumIdx") val albumIdx: Int,
    @SerializedName("singer") val singer: String,
    @SerializedName("title") val title:String,
    @SerializedName("coverImgUrl") val coverImgUrl : String
)
