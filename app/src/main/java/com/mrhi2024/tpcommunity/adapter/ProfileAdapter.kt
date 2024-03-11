package com.mrhi2024.tpcommunity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mrhi2024.tpcommunity.data.ProfileItem
import com.mrhi2024.tpcommunity.databinding.RecyclerProfileFragmentItemBinding

class ProfileAdapter(val context: Context, val itemList: List<ProfileItem>): Adapter<ProfileAdapter.VH>() {
    inner class VH(val binding: RecyclerProfileFragmentItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclerProfileFragmentItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = itemList[position]


        Glide.with(context).load(item.iv).into(holder.binding.iv1)
    }

}