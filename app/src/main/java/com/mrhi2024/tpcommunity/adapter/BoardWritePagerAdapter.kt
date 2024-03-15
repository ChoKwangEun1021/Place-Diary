package com.mrhi2024.tpcommunity.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mrhi2024.tpcommunity.databinding.BoardImagePageBinding

class BoardWritePagerAdapter(val context: Context, val imgs: MutableList<Uri?>) : Adapter<BoardWritePagerAdapter.VH>() {

    inner class VH(val binding: BoardImagePageBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = BoardImagePageBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = imgs.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        Glide.with(context).load(imgs[position]).into(holder.binding.ivPage)
    }
}