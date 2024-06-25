package com.example.myfirstapp.data.remote

import com.example.myfirstapp.data.entities.Album

interface CommunicationInterface {
    fun sendData(album: Album)
}