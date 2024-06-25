package com.example.myfirstapp

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure(message : String)
}