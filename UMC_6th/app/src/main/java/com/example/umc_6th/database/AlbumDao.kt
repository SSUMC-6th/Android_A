
package com.example.umc_6th.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.umc_6th.models.Like
import com.example.umc_6th.music.Album

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id : Int): Album

    @Insert
    fun likeAlbum(like : Like)

    @Query("select id from LikeTable where userId =:userId and albumId = :albumId")
    fun isLikedAlbum(userId : Int, albumId : Int) : Int?

    @Query("delete from LikeTable where userId =:userId and albumId = :albumId")
    fun dislikedAlbum(userId : Int, albumId : Int)

    @Query("select at.* from LikeTable as lt left join AlbumTable as at on lt.albumId = at.id where lt.userId = :userId")
    fun getLikedAlbums(userId : Int) : List<Album>

}
