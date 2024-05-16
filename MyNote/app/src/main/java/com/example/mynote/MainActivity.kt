package com.example.mynote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mynote.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var editText: EditText
    private var savedText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editText = binding.editTextMemo

        binding.editTextMemo.setOnClickListener {
            val intent = Intent(this@MainActivity, ConfirmActivity::class.java)
            intent.putExtra("note", binding.editTextMemo.text.toString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (savedText.isNotEmpty()) {
            editText.setText(savedText)
        }
    }

    override fun onPause() {
        super.onPause()
        savedText = editText.text.toString()
    }

    override fun onRestart() {
        super.onRestart()
        AlertDialog.Builder(this)
            .setMessage("다시 작성하시겠습니까?")
            .setPositiveButton("네", null)
            .setNegativeButton("아니오") { _, _ ->
                savedText = ""
            }
            .show()
    }
}