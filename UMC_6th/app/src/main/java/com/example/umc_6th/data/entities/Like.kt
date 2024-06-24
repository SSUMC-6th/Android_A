package com.example.umc_6th.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LikeTable")
data class Like(var userId: Int, var albumId: Int) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}