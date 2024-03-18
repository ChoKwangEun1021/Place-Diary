package com.mrhi2024.tpcommunity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mrhi2024.tpcommunity.G
import com.mrhi2024.tpcommunity.R
import com.mrhi2024.tpcommunity.activites.BoardWriteActivity
import com.mrhi2024.tpcommunity.adapter.ListAdapter
import com.mrhi2024.tpcommunity.data.Board
import com.mrhi2024.tpcommunity.data.ListItem
import com.mrhi2024.tpcommunity.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val listItem = mutableListOf<Board>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        listItem.add(0, Board("둥이", "이 내용은 테스트용", "이 내용은 테스트중입니다!!", 77, 56))
//        listItem.add(0, Board("둥이", "이 내용은 테스트용", "이 내용은 테스트중입니다!!", 77, 56))
//        listItem.add(0, Board("둥이", "이 내용은 테스트용", "이 내용은 테스트중입니다!!", 77, 56))
//        listItem.add(0, Board("둥이", "이 내용은 테스트용", "이 내용은 테스트중입니다!!", 77, 56))
//        listItem.add(0, Board("둥이", "이 내용은 테스트용", "이 내용은 테스트중입니다!!", 77, 56))
//        listItem.add(0, Board("둥이", "이 내용은 테스트용", "이 내용은 테스트중입니다!!", 77, 56))
//        listItem.add(0, Board("둥이", "이 내용은 테스트용", "이 내용은 테스트중입니다!!", 77, 56))
//        listItem.add(0, Board("둥이", "이 내용은 테스트용", "이 내용은 테스트중입니다!!", 77, 56))
//        listItem.add(0, Board("둥이", "이 내용은 테스트용", "이 내용은 테스트중입니다!!", 77, 56))

        getBoard()
        binding.recyclerListFragment.adapter = ListAdapter(requireContext(), listItem)
        return binding.root
    }

    private fun getBoard() {
        val boardRef = Firebase.firestore.collection("board")

        boardRef.get().addOnSuccessListener {
            for (snapshot: DocumentSnapshot in it) {
                val data = snapshot.data!!
                val nickName = data["nickName"].toString()
                val content = data["content"].toString()
                val title = data["title"].toString()
                listItem.add(0, Board(nickName, title, content))

//                val item = snapshot.toObject(Board::class.java)
//                AlertDialog.Builder(requireContext()).setMessage("$listItem").create().show()
            }

        }

//        binding.recyclerListFragment.adapter = ListAdapter(requireContext(), listItem)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.fab.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    BoardWriteActivity::class.java
                )
            )
        }

        binding.recyclerListFragment.adapter!!.notifyDataSetChanged()


    }
}