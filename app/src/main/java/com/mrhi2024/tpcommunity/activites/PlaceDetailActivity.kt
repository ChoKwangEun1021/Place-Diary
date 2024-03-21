package com.mrhi2024.tpcommunity.activites

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.google.gson.Gson
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.data.Place
import com.mrhi2024.tpcommunity.databinding.ActivityPlaceDetailBinding

class PlaceDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPlaceDetailBinding.inflate(layoutInflater) }
    private lateinit var place: Place
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //인텐트로 부터 데이터 전달받기
        val s: String? = intent.getStringExtra("place")
        s?.also {
            //json --> 객체
            place = Gson().fromJson(it, Place::class.java)

            //웹뷰를 사용할때 반드시 해야 할 3가지 설정
            binding.wv.webViewClient = WebViewClient() //현제 웹뷰안에서 웹문서를 열리도록
            binding.wv.webChromeClient = WebChromeClient() //웹문서안에서 다이얼로그나 팝업 같은 것들이 발동하도록

            binding.wv.settings.javaScriptEnabled = true //웹뷰는 기본적으로 보안문제로 JS 동작을 막아놓았기에 허용하도록

            binding.wv.loadUrl(place.place_url)

        }
    }
}