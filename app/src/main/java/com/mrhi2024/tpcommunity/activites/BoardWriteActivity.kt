package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.mrhi2024.tpcommunity.G
import com.mrhi2024.tpcommunity.databinding.ActivityBoardWriteBinding
import com.mrhi2024.tpcommunity.firebase.FBRef
import com.mrhi2024.tpcommunity.fragment.MapFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BoardWriteActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBoardWriteBinding.inflate(layoutInflater) }
//    private var imgUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener { clickSave() }
        binding.ivBoard.setOnClickListener { getImage() }

        if (intent != null && intent.hasExtra("map")) {
            binding.btnSave.setOnClickListener { clickSave2() }
        }

    }

    private fun clickSave2() {
        val board = mutableMapOf<String, String>()
        val intent = Intent()

        board["title"] = binding.editTextTitle.text.toString()
        board["content"] = binding.inputLayoutContent.editText!!.text.toString()
        board["boardUid"] = G.userUid
        board["nickName"] = G.userNickname
        board["imgUrl"] = fileName

        intent.putExtra("uid", G.userUid)
        intent.putExtra("nickName", G.userNickname)
        intent.putExtra("title", binding.editTextTitle.text.toString())
        intent.putExtra("content", binding.inputLayoutContent.editText!!.text.toString())
        intent.putExtra("imgUrl", fileName)
        setResult(RESULT_OK, intent)
        if (binding.ivBoard.drawable is VectorDrawable) {
            Toast.makeText(this, "사진을 선택해주세요!", Toast.LENGTH_SHORT).show()
            return
        } else if (binding.editTextTitle.text.toString().isEmpty() && binding.inputLayoutContent.editText!!.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "제목 또는 게시글 내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
            return
        } else {
            FBRef.boardRef.document(documentName).set(board).addOnSuccessListener {
                Toast.makeText(this, "작성 완료", Toast.LENGTH_SHORT).show()
            }
            boardImgUpload()
            finish()
        }


    }

    private fun getImage() {

        if (binding.ivBoard.drawable is VectorDrawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pickMultipleLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            } else {
                val intent = Intent(
                    Intent.ACTION_OPEN_DOCUMENT
                ).setType("image/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                resultLauncher.launch(intent)
            }
        } else {
            val intent = Intent(this, BoardWriteImgDetailActivity::class.java)
            val imgsG = Gson().toJson(imgs)
            intent.putExtra("imgs", imgsG)
            startActivity(intent)
        }


//        val intent =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES) else Intent(
//                Intent.ACTION_OPEN_DOCUMENT
//            ).setType("image/*")
//        resultLauncher.launch(intent)


    }

    val imgs = mutableListOf<Uri?>()

    private val pickMultipleLauncher =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            imgs.clear()

            for (uri in it) imgs.add(uri)

            Glide.with(this).load(imgs[0]).into(binding.ivBoard)

        }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            imgUri = it.data?.data
//            Glide.with(this).load(imgUri).into(binding.ivBoard)

            if (it.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "이미지를 선택하지 않았습니다", Toast.LENGTH_SHORT).show()
            } else {
                imgs.clear()
                //1개만 선택했을때는 uri data로 전달받음
                if (it.data?.data != null) {
                    imgs.add(it.data?.data)
                } else { //2개 이상일때는 ClipData 라는 것으로 여러 파일들의 정보를 받음
                    val cnt: Int = it.data?.clipData?.itemCount!!
                    for (i in 0 until cnt) {
                        imgs.add(it.data?.clipData?.getItemAt(i)?.uri)
                    }
                }
            }

        }

    val fileName = "IMG_" + SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date())
    val documentName = SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date())
    private fun clickSave() {
        val board = mutableMapOf<String, String>()

        board["title"] = binding.editTextTitle.text.toString()
        board["content"] = binding.inputLayoutContent.editText!!.text.toString()
        board["boardUid"] = G.userUid
        board["nickName"] = G.userNickname
        board["imgUrl"] = fileName

        if (binding.ivBoard.drawable is VectorDrawable) {
            Toast.makeText(this, "사진을 선택해주세요!", Toast.LENGTH_SHORT).show()
            return
        } else if (binding.editTextTitle.text.toString().isEmpty() && binding.inputLayoutContent.editText!!.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "제목 또는 게시글 내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
            return
        } else {
            FBRef.boardRef.document(documentName).set(board).addOnSuccessListener {
                Toast.makeText(this, "작성 완료", Toast.LENGTH_SHORT).show()
            }
            boardImgUpload()
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    private fun boardImgUpload() {
//        val name = G.userUid

        imgs.apply {
            for (i in 0 until imgs.size) {
                this[i]?.let {
//                    val fileName = "IMG_" + SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date())
                    val imgRef = Firebase.storage.getReference("boardImg/${fileName}")
                    imgRef.putFile(it).addOnSuccessListener {
//                        Toast.makeText(this@BoardWriteActivity, "이미지 업로드 완료", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}