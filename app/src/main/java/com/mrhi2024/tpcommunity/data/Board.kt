package com.mrhi2024.tpcommunity.data

data class Board(
    val nickName: String = "",
    val title: String = "",
    val content: String = "",
    val likeCount: Int = 0,
    val commentCount: Int = 0
)
