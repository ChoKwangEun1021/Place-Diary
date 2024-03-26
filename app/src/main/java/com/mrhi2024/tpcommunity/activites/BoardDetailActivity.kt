package com.mrhi2024.tpcommunity.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.databinding.ActivityBoardDetailBinding
import com.mrhi2024.tpcommunity.firebase.FBRef

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
            val imgUrl = intent.getStringExtra("imgUrl")
            FBRef.labelRef.whereEqualTo("imgUrl", imgUrl).get().addOnSuccessListener {
                for (snapshot: DocumentSnapshot in it) {
                    val data = snapshot.data!!
                    val profile = data["uid"].toString()
                    val nickName = data["nickName"].toString()
                    val title = data["title"].toString()
                    val imgContent = data["imgUrl"].toString()
                    val content = data["content"].toString()

                    val userImgUrl = Firebase.storage.getReference("userImg/${profile}")
                    userImgUrl.downloadUrl.addOnSuccessListener { uri ->
                        Glide.with(this).load(uri).into(binding.ivProfile)
                    }

                    binding.tvName.text = nickName
                    binding.tvTitle.text = title

                    val boardImgUrl = Firebase.storage.getReference("boardImg/${imgContent}")
                    boardImgUrl.downloadUrl.addOnSuccessListener { uri ->
                        Glide.with(this).load(uri).into(binding.ivContent)
                    }

                    binding.tvContent.text = content
                }

            }
        }



    }
}