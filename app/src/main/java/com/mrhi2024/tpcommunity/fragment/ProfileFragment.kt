package com.mrhi2024.tpcommunity.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.mrhi2024.tpcommunity.BuildConfig
import com.mrhi2024.tpcommunity.G
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.activites.LoginActivity
import com.mrhi2024.tpcommunity.adapter.ProfileAdapter
import com.mrhi2024.tpcommunity.data.ProfileItem
import com.mrhi2024.tpcommunity.databinding.FragmentProfileBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val listItem = mutableListOf<ProfileItem>()
    private val spf by lazy { activity?.getSharedPreferences("loginSave", Context.MODE_PRIVATE) }
    private val spf2 by lazy { activity?.getSharedPreferences("loginInfo", Context.MODE_PRIVATE) }
    private val spfEdit by lazy { spf?.edit() }
    private val spfEdit2 by lazy { spf2?.edit() }
    private val auth by lazy { Firebase.auth }

    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        getProfile()

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        NaverIdLoginSDK.initialize(
            requireContext(),
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            "PlaceDiary"
        )

        listItem.add(0, ProfileItem(R.drawable.cat3))
        listItem.add(0, ProfileItem(R.drawable.cat3))
        listItem.add(0, ProfileItem(R.drawable.cat3))
        listItem.add(0, ProfileItem(R.drawable.cat3))
        listItem.add(0, ProfileItem(R.drawable.cat3))
        listItem.add(0, ProfileItem(R.drawable.cat3))

        binding.recyclerProfileFragment.adapter = ProfileAdapter(requireContext(), listItem)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding.ivSetting.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        binding.settingDrawerLayout.btnLogout.setOnClickListener { logOut() }
    }

    private fun logOut() {
        val kakaoAccount = AuthApiClient.instance.hasToken()
        val naverAccount = NaverIdLoginSDK.getAccessToken()
        val googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext())

        if (kakaoAccount) {

            AlertDialog.Builder(requireContext()).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인") { dialog, id ->
                    UserApiClient.instance.logout { error ->
                        if (error != null) {
                            Log.e("logout error", "${error.message}")
                        } else {
                            spfEdit?.putBoolean("isLogin", false)
                            spfEdit2?.clear()
                            spfEdit?.apply()
                            spfEdit2?.apply()
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                            activity?.finish()
                            Toast.makeText(requireContext(), "카카오 로그아웃 완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.setNegativeButton("취소") { dialog, id ->
                dialog.dismiss()
            }.create().show()

        } else if (naverAccount != null) {
            AlertDialog.Builder(requireContext()).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인") { dialog, id ->
                    NaverIdLoginSDK.logout()
//                    NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
//                        override fun onError(errorCode: Int, message: String) {
//                            onFailure(errorCode, message)
//                        }
//
//                        override fun onFailure(httpStatus: Int, message: String) {
//                            Log.d("naver logout", message)
//                        }
//
//                        override fun onSuccess() {
//                            Toast.makeText(requireContext(), "네이버 토근 삭제 성공", Toast.LENGTH_SHORT).show()
//                        }
//
//                    })
                    spfEdit?.putBoolean("isLogin", false)
                    spfEdit2?.clear()
                    spfEdit?.apply()
                    spfEdit2?.apply()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    activity?.finish()
                    Toast.makeText(requireContext(), "네이버 로그아웃 완료", Toast.LENGTH_SHORT).show()
                }.setNegativeButton("취소") { dialog, id ->
                dialog.dismiss()
            }.create().show()

        } else if (googleAccount != null) {

            AlertDialog.Builder(requireContext()).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인") { dialog, id ->
                    googleSignInClient.signOut().addOnSuccessListener {
                        spfEdit?.putBoolean("isLogin", false)
                        spfEdit2?.clear()
                        spfEdit?.apply()
                        spfEdit2?.apply()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        activity?.finish()
                        Toast.makeText(requireContext(), "구글 로그아웃 완료", Toast.LENGTH_SHORT).show()
                    }
                }.setNegativeButton("취소") { dialog, id ->
                dialog.dismiss()
            }.create().show()

        } else {

            AlertDialog.Builder(requireContext()).setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인") { dialog, id ->
                    auth.signOut()
                    spfEdit?.putBoolean("isLogin", false)
                    spfEdit2?.clear()
                    spfEdit?.apply()
                    spfEdit2?.apply()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    activity?.finish()
                    Toast.makeText(requireContext(), "이메일 로그아웃 완료", Toast.LENGTH_SHORT).show()
                }.setNegativeButton("취소") { dialog, id ->
                dialog.dismiss()
            }.create().show()

        }

    }

    private fun getProfile() {
        val imgUrl = Firebase.storage.getReference("userImg/${G.userUid}")
        imgUrl.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it).into(binding.ivProfile)
        }

        binding.tvName.text = G.userNickname
    }
}