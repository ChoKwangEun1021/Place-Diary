package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mrhi2024.tpcommunity.databinding.ActivityEmailLoginBinding
import com.mrhi2024.tpcommunity.firebase.FBauth

class EmailLoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEmailLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        val email = binding.inputLayoutEmail.editText!!.text.toString()
        val password = binding.inputLayoutPassword.editText!!.text.toString()

        FBauth.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}