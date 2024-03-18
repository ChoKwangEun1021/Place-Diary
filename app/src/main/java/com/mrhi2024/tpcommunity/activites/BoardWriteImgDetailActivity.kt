package com.mrhi2024.tpcommunity.activites

import android.graphics.ImageDecoder.ImageInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.mrhi2024.tpcommunity.data.BoardImgs
import com.mrhi2024.tpcommunity.databinding.ActivityBoardWriteImgDetailBinding

class BoardWriteImgDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBoardWriteImgDetailBinding.inflate(layoutInflater) }
    private val imgs = mutableListOf<BoardImgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imgsG = intent.getStringExtra("imgs")
//        imgs.add(aa)

//        AlertDialog.Builder(this).setMessage("$imgs").create().show()

    }
}
