package com.example.umc_6th.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_6th.databinding.ActivitySplashBinding
import com.example.umc_6th.ui.main.MainActivity

class SplashActivity :AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 자동 로그인 시도
       // attemptAutoLogin()

        android.os.Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(0, 0)
        }, 2000)
    }

/* 자동 로그인 splash
    private fun attemptAutoLogin() {
        val token = loadJwtToken()
        if (token.isNullOrEmpty()) {
            navigateToLoginActivity()
        } else {
            RetrofitInstance.authApi.autoLogin("Bearer $token").enqueue(object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    if (response.isSuccessful && response.body()?.code == 1000) {
                        navigateToMainActivity()
                    } else {
                        navigateToLoginActivity()
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    navigateToLoginActivity()
                }
            })
        }
    }

    private fun loadJwtToken(): String? {
        // SharedPreferences에서 JWT 토큰 로드
        return getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("jwt_token", null)
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java)) // MainActivity로 이동
        finish()
    }

    private fun navigateToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java)) //LoginActivity로 이동
        finish()
    }

 */
}