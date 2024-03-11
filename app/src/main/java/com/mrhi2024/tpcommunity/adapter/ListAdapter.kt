package com.mrhi2024.tpcommunity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mrhi2024.tpcommunity.data.ListItem
import com.mrhi2024.tpcommunity.databinding.RecyclerListFragmentItemBinding

class ListAdapter(val context: Context, val itemList: List<ListItem>): Adapter<ListAdapter.VH>() {

    inner class VH(val binding: RecyclerListFragmentItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclerListFragmentItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = itemList[position]

        Glide.with(context).load(item.ivProfile).into(holder.binding.ivProfile)
        holder.binding.tvName.text = item.tvName
        holder.binding.tvName2.text = item.tvName2
        Glide.with(context).load(item.ivList).into(holder.binding.ivList)
        Glide.with(context).load(item.ivContent).into(holder.binding.ivContent)
        Glide.with(context).load(item.ivFavorite).into(holder.binding.ivFavorite)
        Glide.with(context).load(item.ivChat).into(holder.binding.ivChat)
        holder.binding.tvLike.text = item.tvLike
        holder.binding.tvLikeCount.text = item.tvLikeCount.toString()
        holder.binding.tvContent.text = item.tvContent
        holder.binding.tvChat.text = item.tvChat
        holder.binding.tvChatCount.text = item.tvChatCount.toString()

    }
}