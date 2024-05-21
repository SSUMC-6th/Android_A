package com.example.umc_6th

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.umc_6th.databinding.ActivitySongBinding
import kotlin.math.log

class SongActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongBinding
    private var maxBarWidth = 0
    private var timerThread: Thread? = null
    @Volatile private var isTimerRunning: Boolean = false
    private var elapsedSeconds = 0
    private val totalSeconds = 60
    //한곡재생
    @Volatile private var isRepeatOne: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val songTitle = intent.getStringExtra("songTitle")
        val songArtist = intent.getStringExtra("songArtist")

        Log.d("SongActivity", "Received title: $songTitle, artist: $songArtist")

        binding.txSongTitle.text = songTitle
        binding.txSongArtist.text = songArtist

        setupColorFilters()
        setupButtonListeners()

        binding.imgSongDown.setOnClickListener {
            val returnIntent = Intent().apply {
                putExtra("albumTitle", "LILAC")
                putExtra("elapsedSeconds", elapsedSeconds)
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        binding.viewSongBar.post{
            maxBarWidth = binding.viewSongBar.width
            binding.viewSongBarBlue.layoutParams.width=1
        }

    }

    private fun setupColorFilters() {
        binding.imgSongRandom.setOnClickListener {
            if (binding.imgSongRandom.colorFilter != null) {
                binding.imgSongRandom.clearColorFilter()
            } else {
                binding.imgSongRandom.setColorFilter(ContextCompat.getColor(this, R.color.flo), PorterDuff.Mode.SRC_IN)
            }
        }

        binding.imgSongRepeat.setOnClickListener {
            if (binding.imgSongRepeat.colorFilter != null) {
                binding.imgSongRepeat.clearColorFilter()
            } else {
                binding.imgSongRepeat.setColorFilter(ContextCompat.getColor(this, R.color.flo), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    private fun setupButtonListeners() {
        binding.imgSongPlayBtn.setOnClickListener {
            isTimerRunning = true
            binding.imgSongPlayPauseBtn.visibility = View.VISIBLE
            binding.imgSongPlayBtn.visibility = View.GONE
            startOrResumeTimer()
        }

        binding.imgSongPlayPauseBtn.setOnClickListener {
            isTimerRunning = false
            binding.imgSongPlayBtn.visibility = View.VISIBLE
            binding.imgSongPlayPauseBtn.visibility = View.GONE
        }

        binding.imgSongRepeat.setOnClickListener {
            isRepeatOne = !isRepeatOne //반복 재생 상태
            if (!isRepeatOne) {
                binding.imgSongRepeat.setColorFilter(
                    ContextCompat.getColor(this, R.color.flo),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.imgSongRepeat.clearColorFilter()
            }
        }
    }

    private fun startOrResumeTimer() {
        if (timerThread == null || !timerThread!!.isAlive) {
            startTimer(totalSeconds)
        }
    }

    private fun startTimer(totalSeconds: Int) {
        timerThread = Thread {
            while (elapsedSeconds <= totalSeconds && isTimerRunning) {
                updateUI()
                Thread.sleep(1000)
                elapsedSeconds++
                if (elapsedSeconds > totalSeconds) {
                    if (isRepeatOne) { // 반복 재생 상태일 때
                        elapsedSeconds = 0  // 시간을 초기화하고
                        updateUI()  // UI를 초기 상태로 갱신
                        continue  // 반복
                    }
                    break  // 반복 재생이 아닐 때는 중지
                }
            }
        }.apply { start() }
    }

    private fun updateUI() {
        val minutes = elapsedSeconds / 60
        val seconds = elapsedSeconds % 60
        val newWidth = maxBarWidth * elapsedSeconds / totalSeconds
        runOnUiThread {
            val layoutParams = binding.viewSongBarBlue.layoutParams
            layoutParams.width = newWidth
            binding.viewSongBarBlue.layoutParams = layoutParams
            binding.txSongBarStartTime.text = String.format("%02d:%02d", minutes, seconds)
        }
    }
}