package com.example.umc_6th

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_6th.databinding.ActivityMemoBinding

class MemoActivity : AppCompatActivity() {

    lateinit var binding : ActivityMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener {
            val intent = Intent(this, MemoCheckActivity::class.java)
            val memoTxt = binding.memoEt.text.toString()
            intent.putExtra("memo", memoTxt)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = getSharedPreferences("memo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val tempMemo = binding.memoEt.text.toString()
        if(tempMemo.isNotEmpty()) {
            editor.putString("tempMemo", tempMemo)
            Log.d("tempMemo", tempMemo)
            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("memo", MODE_PRIVATE)
        val tempMemo = sharedPreferences.getString("tempMemo", null)

        if(tempMemo != null) {
            binding.memoEt.setText(tempMemo)
        }
    }
}