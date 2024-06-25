package com.example.yourapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_6th.auth.SignUpActivity
import com.example.umc_6th.databinding.ActivityLoginBinding
import com.example.umc_6th.ui.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 기존 로그인 버튼 클릭 이벤트
        binding.loginSignInBtn.setOnClickListener {
            login()
        }

        // 회원가입 버튼 클릭 이벤트
        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // 카카오 로그인 버튼 클릭 이벤트
        binding.loginKakakoLoginIv.setOnClickListener {
            loginWithKakao()
        }
    }

    private fun login() {
        // 기존 로그인 로직
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        // 로그인 검증 로직, 서버 통신 등
        // 로그인 성공 후 메인 액티비티로 이동 예시
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun loginWithKakao() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                handleLoginResult(token, error)
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                handleLoginResult(token, error)
            }
        }
    }

    private fun handleLoginResult(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            Log.e("LoginActivity", "카카오 로그인 실패", error)
            Toast.makeText(this, "로그인 실패: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
        } else if (token != null) {
            Log.i("LoginActivity", "카카오 로그인 성공 ${token.accessToken}")
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            // 로그인 성공 시 MainActivity로 이동
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
