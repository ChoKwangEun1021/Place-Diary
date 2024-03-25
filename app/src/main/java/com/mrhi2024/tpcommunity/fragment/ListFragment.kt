package com.mrhi2024.tpcommunity.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mrhi2024.tpcommunity.G
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.activites.BoardWriteActivity
import com.mrhi2024.tpcommunity.activites.LoginActivity
import com.mrhi2024.tpcommunity.adapter.ListAdapter
import com.mrhi2024.tpcommunity.data.Board
import com.mrhi2024.tpcommunity.data.ListItem
import com.mrhi2024.tpcommunity.databinding.FragmentListBinding
import com.mrhi2024.tpcommunity.firebase.FBRef

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val listItem = mutableListOf<Board>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

//        listItem.add(0, Board("둥이", "이 내용은 테스트용", "이 내용은 테스트중입니다!!", 77, 56))

        binding.recyclerListFragment.adapter = ListAdapter(requireContext(), listItem)
        getBoard()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.fab.setOnClickListener {
            if (isLogin()) {
                startActivity(Intent(requireContext(), BoardWriteActivity::class.java))
            } else {
                AlertDialog.Builder(requireContext()).setTitle("로그인 확인").setMessage("로그인을 해주셔야 이용 가능합니다!!")
                    .setPositiveButton("확인") { dialog, id ->
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        activity?.finish()
                    }.setNegativeButton("취소") { dialog, id ->
                        dialog.dismiss()
                    }.create().show()
            }

        }

        binding.refreshLayout.setOnRefreshListener {
            binding.recyclerListFragment.adapter!!.notifyDataSetChanged()
            binding.refreshLayout.isRefreshing = false
        }

    }

    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBoard() {

        FBRef.boardRef.get().addOnSuccessListener {
            for (snapshot: DocumentSnapshot in it) {
                val data = snapshot.data!!
                val nickName = data["nickName"].toString()
                val content = data["content"].toString()
                val title = data["title"].toString()
                val uid = data["boardUid"].toString()
                val imgUrl = data["imgUrl"].toString()
                listItem.add(Board(nickName, uid, title, content, imgUrl = imgUrl))

            }
            listItem.reverse()
            binding.recyclerListFragment.adapter!!.notifyDataSetChanged()

        }

    }

    private fun isLogin(): Boolean {
        val spf = requireContext().getSharedPreferences("loginSave", AppCompatActivity.MODE_PRIVATE)
        return spf.getBoolean("isLogin", false)
    }

}