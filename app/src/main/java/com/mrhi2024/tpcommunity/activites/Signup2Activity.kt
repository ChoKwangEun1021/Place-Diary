package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.mrhi2024.tpcommunity.databinding.ActivitySignup2Binding
import com.mrhi2024.tpcommunity.firebase.FBauth

class Signup2Activity : AppCompatActivity() {
    private val binding by lazy { ActivitySignup2Binding.inflate(layoutInflater) }

    private lateinit var nickName: String
    private var imgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.cvProfile.setOnClickListener { getImage() }
        binding.btnRegister.setOnClickListener { clickRegister() }
    }

    private fun getImage() {
        val intent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES) else Intent(
                Intent.ACTION_OPEN_DOCUMENT
            ).setType("image/*")
        resultLauncher.launch(intent)
    }

    private fun clickRegister() {
        nickName = binding.inputLayoutNickName.editText!!.text.toString()
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val userRef = Firebase.firestore.collection("emailUsers")

        if (nickName.isNullOrEmpty()) {
            Toast.makeText(this, "닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show()
            return
        }

        FBauth.auth.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener {
            if (it.isSuccessful) {
                userRef.whereEqualTo("email", email).get().addOnSuccessListener {
                    val user = mutableMapOf<String, String>()
                    user["uid"] = FBauth.getUid()
                    user["email"] = email
                    user["password"] = password
                    user["nickName"] = nickName

                    userRef.document().set(user).addOnSuccessListener {
                        Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                    }
                }
                userProfileImgUpload()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.d("create error", "${it.exception?.message}")
                Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun userProfileImgUpload() {
        val name: String = FBauth.getUid()
        val imgRef: StorageReference = Firebase.storage.getReference("userImg/$name")

        imgUri?.apply {
            imgRef.putFile(this).addOnSuccessListener {
                Log.d("img upload", "이미지 업로드 성공")
            }
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            imgUri = it.data?.data
            Glide.with(this).load(imgUri).into(binding.cvProfile)
        }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}