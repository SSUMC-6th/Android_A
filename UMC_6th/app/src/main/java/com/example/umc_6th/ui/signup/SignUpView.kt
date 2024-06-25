package com.example.umc_6th.ui.signup

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure(message : String)
}