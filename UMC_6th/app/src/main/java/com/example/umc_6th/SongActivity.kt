package com.example.umc_6th

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.PersistableBundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val songTitle = intent.getStringExtra("songTitle")
        val songArtist = intent.getStringExtra("songArtist")

        binding.txSongTitle.text = songTitle
        binding.txSongArtist.text = songArtist

        setupColorFilters()
        setupButtonListeners()

        binding.imgSongDown.setOnClickListener {
            val returnIntent = Intent().apply {
                putExtra("albumTitle", "LILAC")
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
    }

    private fun startOrResumeTimer() {
        if (timerThread == null || !timerThread!!.isAlive) {
            startTimer(totalSeconds)
        }
    }

    private fun startTimer(totalSeconds: Int) {
        timerThread = Thread {
            while (elapsedSeconds <= totalSeconds && isTimerRunning) {
                val minutes = elapsedSeconds / 60
                val seconds = elapsedSeconds % 60
                val newWidth = maxBarWidth * elapsedSeconds / totalSeconds
                runOnUiThread {
                    val layoutParams = binding.viewSongBarBlue.layoutParams
                    layoutParams.width = newWidth
                    binding.viewSongBarBlue.layoutParams = layoutParams
                    binding.txSongBarStartTime.text = String.format("%02d:%02d", minutes, seconds)
                }
                Thread.sleep(1000)
                elapsedSeconds++
            }
        }.apply { start() }
    }
}