package com.mrhi2024.tpcommunity.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mrhi2024.tpcommunity.G

class FBRef {
    companion object {
        val fs = Firebase.firestore

        val userRef = fs.collection("users")
        val boardRef = fs.collection("board")
        val labelRef = fs.collection("labels")
    }
}