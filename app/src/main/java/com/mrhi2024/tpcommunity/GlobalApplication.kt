package com.mrhi2024.tpcommunity

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        //카카오 SDK 초기화 작업
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        //카카오맵 SDK 초기화 작업
        KakaoMapSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}