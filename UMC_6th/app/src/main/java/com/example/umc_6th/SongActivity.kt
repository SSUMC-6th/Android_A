package com.example.umc_6th

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_6th.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val songTitle = intent.getStringExtra("songTitle")
        val songArtist = intent.getStringExtra("songArtist")

        binding.txSongTitle.text = songTitle
        binding.txSongArtist.text = songArtist

        binding.imgSongDown.setOnClickListener {
            val returnIntent = Intent().apply {
                putExtra("albumTitle", "LILAC")
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}