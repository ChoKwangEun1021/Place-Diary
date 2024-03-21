package com.mrhi2024.tpcommunity.network

import com.mrhi2024.tpcommunity.BuildConfig
import com.mrhi2024.tpcommunity.data.KakaoSearch
import com.mrhi2024.tpcommunity.data.NaverLogin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitService {

    //네아로 회원정보 프로필 api 요청 : type-String
//    @GET("/v1/nid/me")
//    fun getNidUserInfo(@Header("Authorization") authorization: String) : Call<String>

    @GET("/v1/nid/me")
    fun getNidUserInfo2(@Header("Authorization") authorization: String) : Call<NaverLogin>

    //카카오 로컬 검색 api요청해주는 코드 만들어줘 우선 응답 type : KakaoSearchPlaceResponse
    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}")
    @GET("/v2/local/search/keyword.json")
    fun searchPlace(@Query("query") query: String, @Query("page") page: Int) : Call<KakaoSearch>

    @Headers("Authorization: KakaoAK 72a873eed91d11f29c92c2acbbb6d6ac")
    @GET("/v2/local/search/keyword.json")
    fun searchPlace2(@Query("query") query: String) : Call<String>

}