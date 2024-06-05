package com.example.umc_6th

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user : User)

    @Query("SELECT * FROM UserTable")
    fun getUsers() : List<User>

    @Query("SELECT * FROM usertable WHERE email = :email AND password = :password")
    fun getUser(email : String ,password : String) : User?
}