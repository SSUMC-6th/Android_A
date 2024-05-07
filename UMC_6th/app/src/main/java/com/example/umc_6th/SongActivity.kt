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
        startTimer(60) // 미리듣기 1분
    }
    private fun startTimer(totalSeconds: Int) {
        var elapsedSeconds = 0
        Thread {
            while (elapsedSeconds <= totalSeconds) {
                val minutes = elapsedSeconds / 60
                val seconds = elapsedSeconds % 6

                runOnUiThread { // 메인 스레드에서 UI 업데이트
                    binding.txSongBarStartTime.text = String.format("%02d:%02d", minutes, seconds)
                }
                Thread.sleep(1000)  // 1초 대기
                elapsedSeconds++
            }
            runOnUiThread {
                binding.txSongBarStartTime.text = "미리듣기는 1분입니다"
            }
        }.start()
    }
}