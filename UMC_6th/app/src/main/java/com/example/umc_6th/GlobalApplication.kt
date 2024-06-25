package com.example.umc_6th

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao Sdk 초기화
        KakaoSdk.init(this, "9869def2f21ef97ba7f905ec34960cbb")
    }
}