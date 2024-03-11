package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mrhi2024.tpcommunity.R

class IntroActivity : AppCompatActivity() {
    private val iv by lazy { findViewById<ImageView>(R.id.iv_logo) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        Glide.with(this).load(R.drawable.cat2).into(iv)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }
}