package com.example.umc_6th.adapter

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val PREFS_NAME = "song"
    private var preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSongId(songId: Int) {
        preferences.edit().putInt("LAST_SONG_ID", songId).apply()
    }

    fun getSongId(): Int {
        return preferences.getInt("LAST_SONG_ID", 0)
    }

    fun saveUserSetting(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getUserSetting(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }



}