package com.example.umc_6th.auth

import com.example.umc_6th.models.Result

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure(message : String)
}