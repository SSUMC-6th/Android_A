package com.example.umc_6th.ui.login

import com.example.umc_6th.data.remote.Result

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure(message : String)
}