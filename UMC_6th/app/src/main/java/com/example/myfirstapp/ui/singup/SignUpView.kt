package com.example.myfirstapp.ui.singup

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure(message : String)
}