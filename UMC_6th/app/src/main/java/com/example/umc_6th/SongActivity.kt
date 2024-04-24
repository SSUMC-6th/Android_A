package com.example.umc_6th

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
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

        binding.imgSongRandom.setOnClickListener(){
            if (binding.imgSongRandom.colorFilter != null) {
                binding.imgSongRandom.clearColorFilter()
            } else {
                binding.imgSongRandom.setColorFilter(R.color.flo, PorterDuff.Mode.SRC_IN)
            }
        }
        binding.imgSongRepeat.setOnClickListener(){
            if (binding.imgSongRepeat.colorFilter != null) {
                binding.imgSongRepeat.clearColorFilter()
            } else {
                binding.imgSongRepeat.setColorFilter(R.color.flo, PorterDuff.Mode.SRC_IN)
            }
        }

        binding.imgSongDown.setOnClickListener {
            val returnIntent = Intent().apply {
                putExtra("albumTitle", "LILAC")
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}