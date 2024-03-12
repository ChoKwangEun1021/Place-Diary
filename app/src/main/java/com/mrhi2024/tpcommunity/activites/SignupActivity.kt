package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordConfirm: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnNext.setOnClickListener { clickNext() }
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun clickNext() {
        val intent = Intent(this, Signup2Activity::class.java)

        email = binding.inputLayoutEmail.editText!!.text.toString()
        password = binding.inputLayoutPassword.editText!!.text.toString()
        passwordConfirm = binding.inputLayoutPasswordConfirm.editText!!.text.toString()

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            Toast.makeText(this, "이메일 또는 비밀번호를 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
        } else if (password != passwordConfirm) {
            AlertDialog.Builder(this).setMessage("비밀번호가 같지 않습니다 다시 입력해주세요.").create().show()
            binding.inputLayoutPasswordConfirm.requestFocus()
            return
        } else {
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            intent.putExtra("login_type", "email")
            startActivity(intent)
        }



    }
}