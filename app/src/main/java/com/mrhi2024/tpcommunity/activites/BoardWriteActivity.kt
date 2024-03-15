package com.mrhi2024.tpcommunity.activites

import android.content.Intent
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mrhi2024.tpcommunity.G
import com.mrhi2024.tpcommunity.adapter.BoardWritePagerAdapter
import com.mrhi2024.tpcommunity.databinding.ActivityBoardWriteBinding
import com.mrhi2024.tpcommunity.firebase.FBRef
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BoardWriteActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBoardWriteBinding.inflate(layoutInflater) }
    private var imgUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener { clickSave() }
        binding.ivBoard.setOnClickListener { getImage() }
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
//            intent.putExtra("imgs", imgs.toString())
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

//            binding.pager.adapter = BoardWritePagerAdapter(this, imgs)
        }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            imgUri = it.data?.data
//            Glide.with(this).load(imgUri).into(binding.ivBoard)

//            if (it.resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "이미지를 선택하지 않았습니다", Toast.LENGTH_SHORT).show()
//            } else {
//                imgs.clear()
//                //1개만 선택했을때는 uri data로 전달받음
//                if (it.data?.data != null) {
//                    imgs.add(it.data?.data)
//                } else { //2개 이상일때는 ClipData 라는 것으로 여러 파일들의 정보를 받음
//                    val cnt: Int = it.data?.clipData?.itemCount!!
//                    for (i in 0 until cnt) {
//                        imgs.add(it.data?.clipData?.getItemAt(i)?.uri)
//                    }
//                }
//                binding.pager.adapter = BoardWritePagerAdapter(this, imgs)
//            }

        }

    private fun clickSave() {
        val board = mutableMapOf<String, String>()

        board["title"] = binding.editTextTitle.text.toString()
        board["content"] = binding.inputLayoutContent.editText!!.text.toString()
        board["boardUid"] = G.userUid

        FBRef.boardRef.document().set(board).addOnSuccessListener {
            Toast.makeText(this, "작성 완료", Toast.LENGTH_SHORT).show()
        }
        boardImgUpload()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun boardImgUpload() {
        val fileName = "IMG_" + SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date())
        val name = G.userUid

        val imgRef = Firebase.storage.getReference("boardImg/${fileName}$name")
        imgUri?.apply {
            imgRef.putFile(this).addOnSuccessListener {
                Toast.makeText(this@BoardWriteActivity, "이미지 업로드 완료", Toast.LENGTH_SHORT).show()
            }
        }
    }
}