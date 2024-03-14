package com.mrhi2024.tpcommunity.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.auth.AuthApiClient
import com.mrhi2024.tpcommunity.G
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.adapter.ProfileAdapter
import com.mrhi2024.tpcommunity.data.ProfileItem
import com.mrhi2024.tpcommunity.databinding.FragmentProfileBinding
import com.navercorp.nid.NaverIdLoginSDK

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val listItem = mutableListOf<ProfileItem>()
    private val spf by lazy { activity?.getSharedPreferences("loginInfo", Context.MODE_PRIVATE) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val imgUrl = Firebase.storage.getReference("userImg/${spf?.getString("uid", null)}")
        imgUrl.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it).into(binding.ivProfile)
        }

        binding.tvName.text = spf?.getString("nickName", null)

//        val naverAccount = NaverIdLoginSDK.getAccessToken()
//        val googleAccount = GoogleSignIn.getLastSignedInAccount(requireContext())
//
//        if (AuthApiClient.instance.hasToken()) {
//
//        } else if (naverAccount != null) {
//
//        } else if (googleAccount != null) {
//
//        } else {
//
//        }

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

//        AlertDialog.Builder(requireContext()).setMessage("${G.userAccount?.uid}\n${G.userAccount?.nickName}").create().show()
    }
}