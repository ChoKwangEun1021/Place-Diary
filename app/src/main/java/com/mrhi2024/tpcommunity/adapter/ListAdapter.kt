package com.mrhi2024.tpcommunity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mrhi2024.tpcommunity.G
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.activites.BoardDetailActivity
import com.mrhi2024.tpcommunity.data.Board
import com.mrhi2024.tpcommunity.data.ListItem
import com.mrhi2024.tpcommunity.databinding.RecyclerListFragmentItemBinding

class ListAdapter(val context: Context, val itemList: List<Board>): Adapter<ListAdapter.VH>() {

    inner class VH(val binding: RecyclerListFragmentItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclerListFragmentItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = itemList[position]

        val userImgUrl = Firebase.storage.getReference("userImg/${item.uid}")
        userImgUrl.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(holder.binding.ivProfile)
        }
//        Glide.with(context).load(item.ivProfile).into(holder.binding.ivProfile)

        holder.binding.tvName.text = item.nickName
//        Glide.with(context).load(item.ivList).into(holder.binding.ivList)
        val boardImgUrl = Firebase.storage.getReference("boardImg/${item.imgUrl}")
        boardImgUrl.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(holder.binding.ivContent)
        }
//        Glide.with(context).load(R.drawable.cat3).into(holder.binding.ivContent)
//        Glide.with(context).load(item.).into(holder.binding.ivFavorite)
//        Glide.with(context).load(item.ivChat).into(holder.binding.ivChat)
//        holder.binding.tvLike.text = item.tvLike
        holder.binding.tvTitle.text = item.title
        holder.binding.tvLikeCount.text = item.likeCount.toString()
        holder.binding.tvContent.text = item.content
//        holder.binding.tvChat.text = item.tvChat
        holder.binding.tvCommentCount.text = item.commentCount.toString()

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, BoardDetailActivity::class.java)
            intent.putExtra("profile", item.uid)
            intent.putExtra("nickName", item.nickName)
            intent.putExtra("title", item.title)
            intent.putExtra("imgContent", item.imgUrl)
            intent.putExtra("content", item.content)
            intent.putExtra("listAdapter", "adapter")
            context.startActivity(intent)
        }

    }
}