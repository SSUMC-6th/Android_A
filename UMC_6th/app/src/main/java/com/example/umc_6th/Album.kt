package com.example.umc_6th

data class Album(
    var title : String? = "",
    var artist : String? = "",
    var coverImage : Int? = null,
    var songs: ArrayList<Song>? = null
)
