package com.example.umc_6th

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_6th.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity(){

    lateinit var binding : ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener{
            signUp()
        }
    }

    private fun getUser() : User{
        val email : String = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd : String = binding.signUpPasswordEt.text.toString()
        val name: String = binding.signUpNameEt.text.toString()

        return User(email, pwd, name)
    }

    private fun signUp() : Boolean{
        if (binding.signUpIdEt.text.toString().isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.signUpPasswordEt.text.toString() !=  binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this,"비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.signUpNameEt.text.toString().isEmpty()){
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return false
        }

        RetrofitInstance.authApi.signUp(getUser()).enqueue(object: Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                Log.d("SignUp-Success", response.toString())
                val response : BaseResponse = response.body()!!
                when(response.code) {
                    1000 -> finish() // 성공
                    2016, 2018 -> {
                        binding.signUpEmailErrorTv.visibility = View.VISIBLE
                        binding.signUpEmailErrorTv.text = response.message
                    }
                }

            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("SignUp-Failure", t.message.toString())
            }
        })
        Log.d("SignUpActivity", "All Finished")

        return true
    }
}