package com.example.umc_6th.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.umc_6th.models.Like
import com.example.umc_6th.models.User
import com.example.umc_6th.music.Album
import com.example.umc_6th.music.Song

@Database(entities = [Song::class, Album::class, User::class, Like::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao() : AlbumDao
    abstract fun userDao() : UserDao

    companion object{
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if(instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}