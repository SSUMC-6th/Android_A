package com.example.umc_6th

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "SongTable")
data class Song(
    var title : String? = "",
    var artist : String? = "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var coverImg: Int? = null,
    var isLike: Boolean = false,
    val albumIdx : Int = 0
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
