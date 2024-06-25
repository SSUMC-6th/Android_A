package com.example.umc_6th.view

import com.example.umc_6th.model.Result

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure(message : String)
}