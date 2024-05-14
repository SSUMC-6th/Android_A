package com.example.umc_6th
import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MemoActivity : AppCompatActivity() {
    private var editTextNote: EditText? = null
    private var savedNoteContent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
        editTextNote = findViewById<EditText>(R.id.editTextMemo)
    }

    override fun onResume() {
        super.onResume()
        if (!savedNoteContent.isEmpty()) {
            editTextNote!!.setText(savedNoteContent)
        }
    }

    override fun onPause() {
        super.onPause()
        savedNoteContent = editTextNote!!.text.toString() // 현재 EditText 내용 저장
    }

    override fun onRestart() {
        super.onRestart()
        AlertDialog.Builder(this) // 다이얼로그 생성
            .setTitle("재작성 확인")
            .setMessage("메모를 새로 작성하시겠습니까?")
            .setPositiveButton("예") { dialog, which ->
                editTextNote!!.setText("")
            }
            .setNegativeButton(
                "아니오"
            ) { dialog, which ->
            }
            .show()
    }
}

