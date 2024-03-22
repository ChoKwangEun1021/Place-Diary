package com.mrhi2024.tpcommunity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mrhi2024.tpcommunity.data.SearchList
import com.mrhi2024.tpcommunity.databinding.RecyclerMapSearchListItemBinding

class MapSearchAdapter(val context: Context, val itemList: List<SearchList>) :
    Adapter<MapSearchAdapter.VH>() {
    private lateinit var itemClickListner: OnItemClickListner

    interface OnItemClickListner {
        fun onItemClick(view: View?, position: Int)
    }

    fun setOnItemClickListner(onItemClickListner: OnItemClickListner) {
        itemClickListner = onItemClickListner
    }

    inner class VH(val binding: RecyclerMapSearchListItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding =
            RecyclerMapSearchListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val items = itemList[position]

        holder.binding.tvName.text = items.name
        holder.binding.tvRoad.text = items.road
        holder.binding.tvAddress.text = if (items.road == "") items.address else items.road

//        if (itemClick != null)

        holder.binding.root.setOnClickListener {
            itemClickListner.onItemClick(it, position)
        }

    }



}