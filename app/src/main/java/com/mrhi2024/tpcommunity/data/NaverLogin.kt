package com.mrhi2024.tpcommunity.data

data class NaverLogin(val resultcode: String, val message: String, val response: List<Item>)

//data class Item(
//    val id: String,
//    val nickname: String,
//    val name: String,
//    val email: String,
//    val gender: String,
//    val age: String,
//    val birthday: String,
//    val profile_image: String,
//    val birthyear: String,
//    val mobile: String
//)

data class Item(val id: String, val email: String)
