package com.example.umc_6th

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.umc_6th.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongBinding
    private var maxBarWidth = 0
    private var timerThread: Thread? = null
    @Volatile private var isTimerRunning: Boolean = false
    private var elapsedSeconds = 0
    private var totalSeconds = 60
    //한곡재생
    @Volatile private var isRepeatOne: Boolean = true


    //songDB
    private var gson: Gson = Gson()
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    //곡재생
    private var mediaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()  // 노래 목록 초기화
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        nowPos = sharedPref.getInt("songId", 0)
        if (songs.isNotEmpty()) {
            updateSongUI(songs[nowPos])
        }
     //   val songTitle = intent.getStringExtra("songTitle")
       // val songArtist = intent.getStringExtra("songArtist")

   //     Log.d("SongActivity", "Received title: $songTitle, artist: $songArtist")

  //      binding.txSongTitle.text = songTitle
    //    binding.txSongArtist.text = songArtist

        setupColorFilters()
        setupButtonListeners(songs[nowPos])

        binding.viewSongBar.post{
            maxBarWidth = binding.viewSongBar.width
            binding.viewSongBarBlue.layoutParams.width=1
        }

        if (songs.isNotEmpty()) {
            nowPos = 0  // 초기 위치 설정
            updateSongUI(songs[nowPos])  // 초기 노래 UI 업데이트
        }
        setupNavigationListeners()

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

    private fun setupButtonListeners(song : Song) {
        binding.imgSongDown.setOnClickListener {
            finish()
        }

        binding.imgSongPlayBtn.setOnClickListener {
            isTimerRunning = true
            binding.imgSongPlayPauseBtn.visibility = View.VISIBLE
            binding.imgSongPlayBtn.visibility = View.GONE
            playSong(song)
            startOrResumeTimer()
            startStopService()
        }

        binding.imgSongPlayPauseBtn.setOnClickListener {
            isTimerRunning = false
            binding.imgSongPlayBtn.visibility = View.VISIBLE
            binding.imgSongPlayPauseBtn.visibility = View.GONE
            startStopService()
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
    private fun restartTimer(){
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
                try {
                    Thread.sleep(1000)
                } catch (ie: InterruptedException) {
                    Thread.currentThread().interrupt() // 인터럽트 상태 복원
                    return@Thread // 스레드를 안전하게 종료
                }
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


    override fun onStart() {
        super.onStart()
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        nowPos = sharedPref.getInt("songId", 0)
        Log.d("SongActivitysongId", nowPos.toString())
        if (songs.isNotEmpty()) {
            updateSongUI(songs[nowPos])
        }
    }
    override fun onPause() {
        super.onPause()
        saveCurrentSongInfo()
        songs[nowPos].second = (songs[nowPos].playTime * binding.viewSongBarBlue.progress) / 100000
        songs[nowPos].isPlaying = false
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }
    override fun onDestroy() {
        super.onDestroy()
        timerThread?.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
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
                restartTimer()
            }
        }

        binding.imgSongPlayPrev.setOnClickListener {
            if (nowPos > 0) {
                nowPos--
                updateSongUI(songs[nowPos])
                restartTimer()
            }
        }
    }

    private fun updateSongUI(song: Song) {
        binding.txSongTitle.text = song.title
        binding.txSongArtist.text = song.artist
        song.coverImg?.let { binding.imgSongAlbum.setImageResource(it) }
        updateHeartIcon(song.isLike)
        totalSeconds = songs[nowPos].playTime
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        binding.txSongBarEndTime.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun toggleLikeStatus() {
        val currentSong = songs[nowPos]
        val newLikeStatus = !currentSong.isLike
        currentSong.isLike = newLikeStatus
        songDB.songDao().updateIsLikeById(newLikeStatus, currentSong.id)
        updateHeartIcon(newLikeStatus)

        if (newLikeStatus) {
            showToast(this, "좋아요 한 곡에 담겼습니다.")
        } else {
            showToast(this, "좋아요 한 곡이 취소되었습니다.")
        }
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
        Log.d("SongActivityNextSongId", nowPos.toString())
        startOrResumeTimer()  // 타이머를 다시 시작
    }

    private fun showToast(context: Context, message: String) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.custom_like_toast,
            findViewById(R.id.custom_toast_container))

        val text: TextView = layout.findViewById(R.id.toast_text)
        text.text = message

        with (Toast(context)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

    private fun saveCurrentSongInfo() {
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("songId", songs[nowPos].id)
            putString("songTitle", songs[nowPos].title)
            putString("songArtist", songs[nowPos].artist)
            putInt("songProgress", elapsedSeconds)
            apply()
        }
    }

    private fun startStopService() {
        if (isServiceRunning(ForegroundService::class.java)) {
            Toast.makeText(this, "Foreground Service Stopped", Toast.LENGTH_SHORT).show()
            stopService(Intent(this, ForegroundService::class.java))
        }
        else {
            Toast.makeText(this, "Foreground Service Started", Toast.LENGTH_SHORT).show()
            startService(Intent(this, ForegroundService::class.java))
        }
    }

    private fun isServiceRunning(inputClass : Class<ForegroundService>) : Boolean {
        val manager : ActivityManager = getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager

        for (service : ActivityManager.RunningServiceInfo in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (inputClass.name.equals(service.service.className)) {
                return true
            }

        }
        return false
    }

    //song init

    private fun playSong(song: Song) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {

            val resourceId = resources.getIdentifier(song.music, "raw", packageName)
            println("song은"+resourceId)
            if (resourceId != 0) {
                val afd = resources.openRawResourceFd(resourceId)
                afd?.let {
                    setDataSource(it.fileDescriptor, it.startOffset, it.length)
                    it.close()
                    prepare()
                    start()

                    setOnCompletionListener {
                        // 노래 재생이 끝났을 때 처리
                        playNextSong() // 다음 곡 재생
                    }

                    setOnErrorListener { mp, what, extra ->
                        mp.reset() // 에러가 발생했을 때 MediaPlayer를 리셋
                        true // 에러 처리 완료
                    }
                }
            }
        }
    }

}