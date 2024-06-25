package com.example.myfirstapp.ui.signin

import com.example.myfirstapp.data.remote.Result

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure(message : String)
}