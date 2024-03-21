package com.mrhi2024.tpcommunity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mrhi2024.tpcommunity.data.SearchList
import com.mrhi2024.tpcommunity.databinding.RecyclerMapSearchListItemBinding

class MapSearchAdapter(val context: Context, val itemList: List<SearchList>): Adapter<MapSearchAdapter.VH>() {
    private lateinit var itemClickListener: OnItemClickListener
    inner class VH(val binding: RecyclerMapSearchListItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclerMapSearchListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val items = itemList[position]

        holder.binding.tvName.text = items.name
        holder.binding.tvRoad.text = items.road
        holder.binding.tvAddress.text = if (items.road == "") items.address else items.road

        holder.binding.root.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View?, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

}