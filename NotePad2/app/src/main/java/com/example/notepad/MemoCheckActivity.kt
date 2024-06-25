package com.example.notepad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad.databinding.ActivityMemocheckBinding

class MemoCheckActivity : AppCompatActivity() {

    lateinit var binding : ActivityMemocheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemocheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("memo")) {
            binding.memoCheckText.text = intent.getStringExtra("memo")!!
        }
    }
}