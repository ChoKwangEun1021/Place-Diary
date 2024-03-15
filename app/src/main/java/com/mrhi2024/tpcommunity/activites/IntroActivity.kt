package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mrhi2024.tpcommunity.G
import com.mrhi2024.tpcommunity.R

class IntroActivity : AppCompatActivity() {
    private val iv by lazy { findViewById<ImageView>(R.id.iv_logo) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val spf = getSharedPreferences("loginInfo", MODE_PRIVATE)
        G.userUid = spf.getString("uid", "").toString()
        G.userNickname = spf.getString("nickName", "").toString()
        Glide.with(this).load(R.drawable.cat2).into(iv)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (isLogin()) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun isLogin(): Boolean {
        val spf = getSharedPreferences("loginSave", MODE_PRIVATE)
        return spf.getBoolean("isLogin", false)
    }
}