package com.example.umc_6th

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "db0a6ee868ee324207d083d8d5de7fcd")
    }
}