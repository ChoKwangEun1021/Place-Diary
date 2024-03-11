package com.mrhi2024.tpcommunity.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.databinding.ActivityMainBinding
import com.mrhi2024.tpcommunity.fragment.HomeFragment
import com.mrhi2024.tpcommunity.fragment.ListFragment
import com.mrhi2024.tpcommunity.fragment.MapFragment
import com.mrhi2024.tpcommunity.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.container, HomeFragment()).commit()

        binding.bnv.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_bnv_home -> supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment()).commit()
                R.id.menu_bnv_place -> supportFragmentManager.beginTransaction().replace(R.id.container, MapFragment()).commit()
                R.id.menu_bnv_list -> supportFragmentManager.beginTransaction().replace(R.id.container, ListFragment()).commit()
                R.id.menu_bnv_profile -> supportFragmentManager.beginTransaction().replace(R.id.container, ProfileFragment()).commit()
            }

            true
        }
    }

}