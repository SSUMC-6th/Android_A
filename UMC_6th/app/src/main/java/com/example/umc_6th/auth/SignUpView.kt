package com.example.umc_6th.auth

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure(message : String)
}