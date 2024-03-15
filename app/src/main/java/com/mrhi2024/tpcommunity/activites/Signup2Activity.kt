package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.mrhi2024.tpcommunity.G
import com.mrhi2024.tpcommunity.data.User
import com.mrhi2024.tpcommunity.databinding.ActivitySignup2Binding

class Signup2Activity : AppCompatActivity() {
    private val binding by lazy { ActivitySignup2Binding.inflate(layoutInflater) }

    private var imgUri: Uri? = null
    private lateinit var nickName: String
    private val auth by lazy { Firebase.auth }
    private val spf by lazy { getSharedPreferences("loginInfo", MODE_PRIVATE) }
    private val spfEdit by lazy { spf.edit() }

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

        if (binding.cvProfile.drawable is VectorDrawable) {
            Toast.makeText(this, "프로필 사진을 선택해주세요!", Toast.LENGTH_SHORT).show()
            return
        } else if (binding.inputLayoutNickName.editText!!.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "닉네임을 입력해주세요!", Toast.LENGTH_SHORT).show()
            return
        } else {
//            Toast.makeText(this, "이미지 선택완료", Toast.LENGTH_SHORT).show()
            signUp()
        }

    }

    private fun signUp() {
        val userRef = Firebase.firestore.collection("users")
        nickName = binding.inputLayoutNickName.editText!!.text.toString()

        if (intent != null && intent.hasExtra("login_type")) {
            when (intent.getStringExtra("login_type")) {
                "kakao" -> {
                    val uid = intent.getStringExtra("kakao_uid")
                    val kakaoEmail = "${uid}@kakao.com"

                    userRef.whereEqualTo("email", kakaoEmail).get().addOnSuccessListener {

                        val user = mutableMapOf<String, String>()
                        user["uid"] = uid.toString()
                        user["email"] = kakaoEmail
                        user["password"] = "카카오로그인 사용"
                        user["nickName"] = nickName

                        spfEdit.putString("uid", uid)
                        spfEdit.putString("nickName", nickName)
                        spfEdit.apply()

                        userRef.document().set(user).addOnSuccessListener {
                            Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                    userProfileImgUpload()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                "naver" -> {
                    val uid = intent.getStringExtra("naver_uid")
                    val naverEmail = intent.getStringExtra("naver_email")

                    userRef.whereEqualTo("email", naverEmail).get().addOnSuccessListener {

                        val user = mutableMapOf<String, String>()
                        user["uid"] = uid.toString()
                        user["email"] = naverEmail.toString()
                        user["password"] = "네이버로그인 사용"
                        user["nickName"] = nickName

                        spfEdit.putString("uid", uid)
                        spfEdit.putString("nickName", nickName)
                        spfEdit.apply()

                        userRef.document().set(user).addOnSuccessListener {
                            Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                    userProfileImgUpload()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                "google" -> {
                    val googleEmail = intent.getStringExtra("google_email").toString()

                    userRef.whereEqualTo("email", googleEmail).get().addOnSuccessListener {
                        val uid = intent.getStringExtra("google_uid")
                        val user = mutableMapOf<String, String>()
                        user["uid"] = uid.toString()
                        user["email"] = googleEmail
                        user["password"] = "구글로그인 사용"
                        user["nickName"] = nickName

                        spfEdit.putString("uid", uid)
                        spfEdit.putString("nickName", nickName)
                        spfEdit.apply()

                        userRef.document().set(user).addOnSuccessListener {
                            Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                    userProfileImgUpload()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                "email" -> {
                    val email3 = intent.getStringExtra("email").toString()
                    val password = intent.getStringExtra("password")
                    val uid = auth.currentUser?.uid.toString()

                    auth.createUserWithEmailAndPassword(email3, password!!)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                userRef.whereEqualTo("email", email3).get().addOnSuccessListener {
                                    val user = mutableMapOf<String, String>()
                                    user["uid"] = uid
                                    user["email"] = email3
                                    user["password"] = password
                                    user["nickName"] = nickName

                                    spfEdit.putString("uid", uid)
                                    spfEdit.putString("nickName", nickName)
                                    spfEdit.apply()

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
            }//end of when
        }
    }

    private fun userProfileImgUpload() {
        var name = ""
        if (intent != null && intent.hasExtra("login_type")) {
            when (intent.getStringExtra("login_type")) {
                "kakao" -> {
                    name = intent.getStringExtra("kakao_uid").toString()

                    val imgRef: StorageReference = Firebase.storage.getReference("userImg/$name")

                    imgUri?.apply {
                        imgRef.putFile(this).addOnSuccessListener {
                            Log.d("img upload", "이미지 업로드 성공")
                        }
                    }

                }

                "naver" -> {
                    name = intent.getStringExtra("naver_uid").toString()

                    val imgRef: StorageReference = Firebase.storage.getReference("userImg/$name")

                    imgUri?.apply {
                        imgRef.putFile(this).addOnSuccessListener {
                            Log.d("img upload", "이미지 업로드 성공")
                        }
                    }
                }

                "google" -> {
                    name = intent.getStringExtra("google_uid").toString()

                    val imgRef: StorageReference = Firebase.storage.getReference("userImg/$name")

                    imgUri?.apply {
                        imgRef.putFile(this).addOnSuccessListener {
                            Log.d("img upload", "이미지 업로드 성공")
                        }
                    }
                }

                "email" -> {
                    name = auth.currentUser?.uid.toString()

                    val imgRef: StorageReference = Firebase.storage.getReference("userImg/$name")

                    imgUri?.apply {
                        imgRef.putFile(this).addOnSuccessListener {
                            Log.d("img upload", "이미지 업로드 성공")
                        }
                    }
                }

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