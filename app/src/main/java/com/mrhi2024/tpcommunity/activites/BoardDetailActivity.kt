package com.mrhi2024.tpcommunity.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.databinding.ActivityBoardDetailBinding

class BoardDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBoardDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent != null && intent.hasExtra("listAdapter")) {
            val profile = intent.getStringExtra("profile")
            val nickName = intent.getStringExtra("nickName")
            val title = intent.getStringExtra("title")
            val imgContent = intent.getStringExtra("imgContent")
            val content = intent.getStringExtra("content")

            val userImgUrl = Firebase.storage.getReference("userImg/${profile}")
            userImgUrl.downloadUrl.addOnSuccessListener {
                Glide.with(this).load(it).into(binding.ivProfile)
            }

            binding.tvName.text = nickName
            binding.tvTitle.text = title

            val boardImgUrl = Firebase.storage.getReference("boardImg/${imgContent}")
            boardImgUrl.downloadUrl.addOnSuccessListener {
                Glide.with(this).load(it).into(binding.ivContent)
            }

            binding.tvContent.text = content

        } else {

        }



    }
}