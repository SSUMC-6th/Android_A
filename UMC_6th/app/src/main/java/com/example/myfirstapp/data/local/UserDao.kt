package com.example.myfirstapp.data.local

import androidx.room.*
import com.example.myfirstapp.data.entities.User

@Dao
interface UserDao {
    @Insert
    fun insert(user : User)

    @Query("select * from UserTable")
    fun getUsers() : List<User>

    @Query("select * from UserTable where email =:email and password = :password")
    fun getUser(email : String, password : String) : User?
}