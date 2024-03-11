package com.mrhi2024.tpcommunity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.adapter.ProfileAdapter
import com.mrhi2024.tpcommunity.data.ProfileItem
import com.mrhi2024.tpcommunity.databinding.FragmentProfileBinding

class ProfileFragment: Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val listItem = mutableListOf<ProfileItem>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

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
        
    }
}