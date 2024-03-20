package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.mrhi2024.tpcommunity.G
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.databinding.ActivityMainBinding
import com.mrhi2024.tpcommunity.fragment.HomeFragment
import com.mrhi2024.tpcommunity.fragment.ListFragment
import com.mrhi2024.tpcommunity.fragment.MapFragment
import com.mrhi2024.tpcommunity.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val spf by lazy { getSharedPreferences("loginInfo", MODE_PRIVATE) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.container, HomeFragment()).commit()

        binding.bnv.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_bnv_home -> supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment()).commit()
                R.id.menu_bnv_place -> supportFragmentManager.beginTransaction().replace(R.id.container, MapFragment()).commit()
                R.id.menu_bnv_list -> supportFragmentManager.beginTransaction().replace(R.id.container, ListFragment()).commit()
                R.id.menu_bnv_profile -> {
                    if (isLogin()) {
                        supportFragmentManager.beginTransaction().replace(R.id.container, ProfileFragment()).commit()
                    } else {
                        AlertDialog.Builder(this).setTitle("로그인 확인").setMessage("로그인을 해주셔야 이용 가능합니다!!")
                            .setPositiveButton("확인") { dialog, id ->
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }.setNegativeButton("취소") { dialog, id ->
                                dialog.dismiss()
                            }.create().show()
                    }
                }
            }
            true
        }

        G.userUid = spf.getString("uid", "").toString()
        G.userNickname = spf.getString("nickName", "").toString()
    }

    private fun isLogin(): Boolean {
        val spf = getSharedPreferences("loginSave", MODE_PRIVATE)
        return spf.getBoolean("isLogin", false)
    }

}