package com.example.umc_6th

import android.app.Activity
import android.content.Context
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
import com.google.gson.Gson
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


    //songDB
    private var gson: Gson = Gson()
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

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

        initPlayList()  // 노래 목록 초기화
        if (songs.isNotEmpty()) {
            nowPos = 0  // 초기 위치 설정
            updateSongUI(songs[nowPos])  // 초기 노래 UI 업데이트
        }
        setupNavigationListeners()

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        nowPos = sharedPref.getInt("LastSongPosition", 0)
        if (songs.isNotEmpty()) {
            updateSongUI(songs[nowPos])
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
        binding.imgSongLike.setOnClickListener {
            toggleLikeStatus()
        }
    }

    private fun startOrResumeTimer() {
        if (timerThread == null || !timerThread!!.isAlive) {
            startTimer(totalSeconds)
        }
    }
    private fun RestartTimer(){
        if (timerThread != null && timerThread!!.isAlive) {
            // 기존 타이머가 실행 중이면 중단
            isTimerRunning = false
            try {
                timerThread!!.join()  // 기존 스레드가 완전히 종료될 때까지 대기
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        // 새로운 타이머 시작
        elapsedSeconds = 0  // 시간을 초기화
        isTimerRunning = true
        startTimer(totalSeconds)
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
                        // 곡 반복 재생 또는 다음 곡 재생 처리
                        if (isRepeatOne) {
                            // 같은 곡을 반복 재생
                            continue
                        } else {
                            // 다음 곡으로 넘어가기
                            runOnUiThread {
                                playNextSong()
                            }
                        } // 반복
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


    override fun onPause() {
        super.onPause()
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("LastSongPosition", nowPos)
            apply()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        timerThread?.interrupt()
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun addSongToDB(song: Song) {
        songDB.songDao().insert(song)
    }

    private fun findPositionBySongId(songId: Int) {
        nowPos = songs.indexOfFirst { it.id == songId }
    }

    private fun setupNavigationListeners() {
        binding.imgSongPlayNext.setOnClickListener {
            if (nowPos < songs.size - 1) {
                nowPos++
                updateSongUI(songs[nowPos])
                RestartTimer()
            }
        }

        binding.imgSongPlayPrev.setOnClickListener {
            if (nowPos > 0) {
                nowPos--
                updateSongUI(songs[nowPos])
                RestartTimer()
            }
        }
    }

    private fun updateSongUI(song: Song) {
        binding.txSongTitle.text = song.title
        binding.txSongArtist.text = song.artist
        song.coverImg?.let { binding.imgSongAlbum.setImageResource(it) }
        updateHeartIcon(song.isLike)
    }

    private fun toggleLikeStatus() {
        val currentSong = songs[nowPos]
        val newLikeStatus = !currentSong.isLike
        currentSong.isLike = newLikeStatus
        songDB.songDao().updateIsLikeById(newLikeStatus, currentSong.id)
        updateHeartIcon(newLikeStatus)
    }


    private fun updateHeartIcon(isLiked: Boolean) {
        if (isLiked) {
            binding.imgSongLike.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.imgSongLike.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun playNextSong() {
        if (nowPos < songs.size - 1) {
            nowPos++
        } else {
            nowPos = 0  // 목록의 첫 번째 곡으로 돌아가기
        }
        updateSongUI(songs[nowPos])
        startOrResumeTimer()  // 타이머를 다시 시작
    }


    }