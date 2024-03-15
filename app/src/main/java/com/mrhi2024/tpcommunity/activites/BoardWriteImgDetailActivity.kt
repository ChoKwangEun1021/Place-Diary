package com.mrhi2024.tpcommunity.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mrhi2024.tpcommunity.adapter.BoardWritePagerAdapter
import com.mrhi2024.tpcommunity.databinding.ActivityBoardWriteImgDetailBinding

class BoardWriteImgDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBoardWriteImgDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        binding.pager.adapter = BoardWritePagerAdapter(this, )
    }
}