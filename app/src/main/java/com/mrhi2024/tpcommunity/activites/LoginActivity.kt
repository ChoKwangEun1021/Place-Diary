package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), OnClickListener {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Glide.with(this).load(R.drawable.night).into(binding.ivNight)

        binding.btnGo.setOnClickListener(this)
        binding.btnLoginKakao.setOnClickListener(this)
        binding.btnLoginNaver.setOnClickListener(this)
        binding.btnLoginGoogle.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.btnSignup.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_go -> {
                startActivity(Intent(this, MainActivity::class.java))
            }

            R.id.btn_login_kakao -> {
                Toast.makeText(this, "카카오 로그인", Toast.LENGTH_SHORT).show()

                //두개의 로그인 요청 콜백 함수
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        Toast.makeText(this, "카카오 로그인 실패 : ${error.message}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "카카오 로그인 성공", Toast.LENGTH_SHORT).show()

                        //사용자 정보 요청
                        UserApiClient.instance.me { user, error ->
                            if (user != null) {
                                val id: String = user.id.toString()
//                                val nickname: String = user.kakaoAccount?.profile?.nickname ?: ""

//                                AlertDialog.Builder(this).setMessage("$id\n$nickname").create().show()
                                //로그인 되었으니
                                val intent = Intent(this, Signup2Activity::class.java)
                                intent.putExtra("uid", id)
                                intent.putExtra("login_type", "kakao")
                                startActivity(intent)
                                finish()
                            }
                        }

                    }
                }

                if (AuthApiClient.instance.hasToken()) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {

                    //카카오톡이 사용가능하면 이를 이용하여 로그인하고 없으면 카카오계정으로 로그인하기
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                        UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    }

                }

            }

            R.id.btn_login_naver -> {
                Toast.makeText(this, "네이버 로그인", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Signup2Activity::class.java))
            }

            R.id.btn_login_google -> {
                Toast.makeText(this, "구글 로그인", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Signup2Activity::class.java))
            }

            R.id.btn_login -> {
                startActivity(Intent(this, EmailLoginActivity::class.java))
            }

            R.id.btn_signup -> {
                startActivity(Intent(this, SignupActivity::class.java))
            }
        }
    }
}