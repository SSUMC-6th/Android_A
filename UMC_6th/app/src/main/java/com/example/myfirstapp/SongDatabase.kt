package com.example.myfirstapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class, Album::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun songDao(): SongDao

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database" // 다른 데이터 베이스랑 이름이 겹치지 않도록 주의
                    ).allowMainThreadQueries().build() // 편의상 메인 쓰레드에서 처리하게 한다.
                }
            }

            return instance
        }
    }
}