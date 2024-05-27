package com.example.umc_6th

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song : Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs() : List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id : Int): Song
}