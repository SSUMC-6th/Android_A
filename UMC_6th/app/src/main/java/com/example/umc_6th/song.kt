package com.example.umc_6th

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
    val title : String = "",
    val singer : String = "",
    var second : Int = 0,
    var playTime : Int = 60,
    var isPlaying : Boolean = false,
    var music : String = "",
    var coverImg : Int? = null,
    var isLike : Boolean = false
){
 @PrimaryKey(autoGenerate = true) var id : Int = 0
}
