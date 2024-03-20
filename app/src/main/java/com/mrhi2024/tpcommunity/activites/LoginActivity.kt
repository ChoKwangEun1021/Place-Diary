package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.mrhi2024.tpcommunity.BuildConfig
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.data.NaverLogin
import com.mrhi2024.tpcommunity.databinding.ActivityLoginBinding
import com.mrhi2024.tpcommunity.firebase.FBRef
import com.mrhi2024.tpcommunity.network.RetrofitHelper
import com.mrhi2024.tpcommunity.network.RetrofitService
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), OnClickListener {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val spf by lazy { getSharedPreferences("loginSave", MODE_PRIVATE) }
    private val spfEdit by lazy { spf.edit() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (spf.getBoolean("isLogin", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

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
                //두개의 로그인 요청 콜백 함수
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        Toast.makeText(this, "카카오 로그인 실패 : ${error.message}", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        //사용자 정보 요청
                        UserApiClient.instance.me { user, error ->
                            if (user != null) {
                                val id: String = user.id.toString()
                                //로그인 되었으니
                                val intent = Intent(this, Signup2Activity::class.java)
                                intent.putExtra("kakao_uid", id)
                                intent.putExtra("login_type", "kakao")
                                spfEdit.putBoolean("isLogin", true)
                                spfEdit.apply()
                                startActivity(intent)
                                finish()
                            }
                        }

                    }
                }

                //카카오 로그인되어있는기 기록 확인
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
                //네아로 SDK 초기화
                NaverIdLoginSDK.initialize(
                    this,
                    BuildConfig.NAVER_CLIENT_ID,
                    BuildConfig.NAVER_CLIENT_SECRET,
                    "PlaceDiary"
                )

                val account = NaverIdLoginSDK.getAccessToken()

                if (account != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {

                    //로그인 요청
                    NaverIdLoginSDK.authenticate(this, object : OAuthLoginCallback {
                        override fun onError(errorCode: Int, message: String) {
                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onSuccess() {
                            Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()

                            //사용자 정보를 받아오기 -- REST API로 받아야 함
                            //로그인에 성공하면 REST API로 요청할 수 있는 토큰(token)을 발급받음
                            val accessToken: String? = NaverIdLoginSDK.getAccessToken()

                            //Retrofit 작업을 통해 사용자 정보 가져오기
                            val retroift =
                                RetrofitHelper.getRetrofitInstance("https://openapi.naver.com")
                            val retrofitApiService = retroift.create(RetrofitService::class.java)

                            val call = retrofitApiService.getNidUserInfo2("Bearer $accessToken")
                            call.enqueue(object : Callback<NaverLogin> {
                                override fun onResponse(
                                    call: Call<NaverLogin>,
                                    response: Response<NaverLogin>
                                ) {
                                    val intent = Intent(this@LoginActivity, Signup2Activity::class.java)
                                    val s = response.body()
                                    val id = s?.response?.id
                                    val email = s?.response?.email
                                    intent.putExtra("naver_uid", id)
                                    intent.putExtra("naver_email", email)
                                    intent.putExtra("login_type", "naver")
                                    spfEdit.putBoolean("isLogin", true)
                                    spfEdit.apply()

//                                    FBRef.userRef.whereEqualTo("uid", id).get().addOnSuccessListener {
//                                        it.forEach {
//                                            val data = it.data
//                                            val uid = data["uid"].toString()
//                                            val nickName = data["nickName"].toString()
//
//
//                                        }
//                                    }

                                    startActivity(intent)
//                                AlertDialog.Builder(this@LoginActivity).setMessage("$id\n$email").create().show()
                                }

                                override fun onFailure(call: Call<NaverLogin>, t: Throwable) {
                                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                                }

                            })

                        }

                    })
                }//else

            }

            R.id.btn_login_google -> {
                //로그인 옵션객체 생성 - Builder - 이메일 요청
                val signInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN
                ).requestEmail().build()

                //구글 로그인 이력 확인하기
                val account = GoogleSignIn.getLastSignedInAccount(this)
                //구글 로그인 상태에 따른 조건문
                if (account != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    //구글 로그인을 하는 화면 액티비티를 실행하는 Intent 객체로 로그인 구현
                    val intent: Intent = GoogleSignIn.getClient(this, signInOptions).signInIntent
                    resultLauncher.launch(intent)
                }

            }

            R.id.btn_login -> {
                startActivity(Intent(this, EmailLoginActivity::class.java))
            }

            R.id.btn_signup -> {
                startActivity(Intent(this, SignupActivity::class.java))
            }
        }

    }

    //구글 로그인화면 결과를 받아주는 대행사 등록
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //로그인 결과를 가져온 인텐트 소환
            val intent: Intent? = it.data
            //인텐트로 부터 구글 계정 정보를 가져오는 작업자 객체를 소환
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

            //작업자로부터 계정 받기
            val account: GoogleSignInAccount = task.result
            val id: String = account.id.toString()
            val email: String = account.email ?: ""

//            Toast.makeText(this, "$id\n$email", Toast.LENGTH_SHORT).show()

            //main 화면으로 이동
            val intent2 = Intent(this, Signup2Activity::class.java)
            intent2.putExtra("google_uid", id)
            intent2.putExtra("google_email", email)
            intent2.putExtra("login_type", "google")
            spfEdit.putBoolean("isLogin", true)
            spfEdit.apply()
            startActivity(intent2)
        }
}