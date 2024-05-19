package com.example.umc_6th

import java.util.ArrayList

data class Album(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var songs: ArrayList<Song>? = null
)
