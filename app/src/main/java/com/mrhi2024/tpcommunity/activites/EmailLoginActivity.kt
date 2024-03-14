package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mrhi2024.tpcommunity.databinding.ActivityEmailLoginBinding
import com.mrhi2024.tpcommunity.firebase.FBAuth

class EmailLoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEmailLoginBinding.inflate(layoutInflater) }
    private val spf by lazy { getSharedPreferences("loginSave", MODE_PRIVATE) }
    private val spfEdit by lazy { spf.edit() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (spf.getBoolean("isLogin", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        val email = binding.inputLayoutEmail.editText!!.text.toString()
        val password = binding.inputLayoutPassword.editText!!.text.toString()

        FBAuth.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                spfEdit.putBoolean("isLogin", true)
                spfEdit.apply()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}