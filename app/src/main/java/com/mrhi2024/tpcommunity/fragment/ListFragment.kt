package com.mrhi2024.tpcommunity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.activites.BoardWriteActivity
import com.mrhi2024.tpcommunity.adapter.ListAdapter
import com.mrhi2024.tpcommunity.data.ListItem
import com.mrhi2024.tpcommunity.databinding.FragmentListBinding

class ListFragment: Fragment() {
    private lateinit var binding: FragmentListBinding
    private val listItem = mutableListOf<ListItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        listItem.add(0, ListItem("둥이", "둥이", "이 내용은 테스트중입니다!!", R.drawable.cat3, 77, 56, R.drawable.desert_fox, R.drawable.favorite))
        listItem.add(0, ListItem("둥이", "둥이", "이 내용은 테스트중입니다!!", R.drawable.cat3, 77, 56, R.drawable.desert_fox, R.drawable.favorite))
        listItem.add(0, ListItem("둥이", "둥이", "이 내용은 테스트중입니다!!", R.drawable.cat3, 77, 56, R.drawable.desert_fox, R.drawable.favorite))
        listItem.add(0, ListItem("둥이", "둥이", "이 내용은 테스트중입니다!!", R.drawable.cat3, 77, 56, R.drawable.desert_fox, R.drawable.favorite))
        listItem.add(0, ListItem("둥이", "둥이", "이 내용은 테스트중입니다!!", R.drawable.cat3, 77, 56, R.drawable.desert_fox, R.drawable.favorite))
        listItem.add(0, ListItem("둥이", "둥이", "이 내용은 테스트중입니다!!", R.drawable.cat3, 77, 56, R.drawable.desert_fox, R.drawable.favorite))
        listItem.add(0, ListItem("둥이", "둥이", "이 내용은 테스트중입니다!!", R.drawable.cat3, 77, 56, R.drawable.desert_fox, R.drawable.favorite))
        listItem.add(0, ListItem("둥이", "둥이", "이 내용은 테스트중입니다!!", R.drawable.cat3, 77, 56, R.drawable.desert_fox, R.drawable.favorite))
        listItem.add(0, ListItem("둥이", "둥이", "이 내용은 테스트중입니다!!", R.drawable.cat3, 77, 56, R.drawable.desert_fox, R.drawable.favorite))
        listItem.add(0, ListItem("둥이", "둥이", "이 내용은 테스트중입니다!!", R.drawable.cat3, 77, 56, R.drawable.desert_fox, R.drawable.favorite))

        binding.recyclerListFragment.adapter = ListAdapter(requireContext(), listItem)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.fab.setOnClickListener { startActivity(Intent(requireContext(), BoardWriteActivity::class.java)) }


    }
}