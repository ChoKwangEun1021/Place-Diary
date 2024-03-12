package com.mrhi2024.tpcommunity.network

import com.mrhi2024.tpcommunity.data.NaverLogin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RetrofitService {

    //네아로 회원정보 프로필 api 요청 : type-String
    @GET("/v1/nid/me")
    fun getNidUserInfo(@Header("Authorization") authorization: String) : Call<String>

//    @GET("/v1/nid/me")
//    fun getNidUserInfo(@Header("Authorization") authorization: String) : Call<NaverLogin>
}