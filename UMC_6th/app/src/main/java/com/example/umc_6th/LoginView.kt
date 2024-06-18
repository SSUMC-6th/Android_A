package com.example.umc_6th

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure(message : String)
}