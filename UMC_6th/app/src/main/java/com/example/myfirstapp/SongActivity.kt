package com.example.myfirstapp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myfirstapp.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    private var isColorChanged = false
    lateinit var song : Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)


      //songDownIb를 누르면 songActivity 종료

       binding.songDownIb.setOnClickListener {
           finish()
       }

        binding.songMiniplayerPlayIv.setOnClickListener{
            setPlayerStatus(false)
        }
        binding.songMiniplayerPauseIv.setOnClickListener{
            setPlayerStatus(true)
        }

        binding.songRepeatIv.setOnClickListener {
            if (isColorChanged) {
                // 변경된 색상을 다시 초기 색상으로 변경
                binding.songRepeatIv.setColorFilter(ContextCompat.getColor(this, R.color.black))
            } else {
                binding.songRepeatIv.setColorFilter(ContextCompat.getColor(this, R.color.flo))
            }
            isColorChanged = !isColorChanged
        }

        binding.songRandomIv.setOnClickListener {
            if (isColorChanged) {
                // 변경된 색상을 다시 초기 색상으로 변경
                binding.songRandomIv.setColorFilter(ContextCompat.getColor(this, R.color.black))
            } else {
                binding.songRandomIv.setColorFilter(ContextCompat.getColor(this, R.color.flo))
            }
            isColorChanged = !isColorChanged
        }
    }

    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }
    private fun setPlayer(song : Song) {
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songMusicSingerTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songSeekbarSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        setPlayerStatus(song.isPlaying)
    }
    fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying
        if(isPlaying){ //재생중
            binding.songMiniplayerPlayIv.visibility = View.VISIBLE
            binding.songMiniplayerPauseIv.visibility = View.GONE
            mediaPlayer?.start()
        }
        else{ //일시정지
            binding.songMiniplayerPlayIv.visibility = View.GONE
            binding.songMiniplayerPauseIv.visibility = View.VISIBLE
            if(mediaPlayer?.isPlaying == true) {  //재생중이 아닐 때 정지하면 오류 발생 가능
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true):Thread(){
        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try{
                while(true){
                    if(second >= playTime){
                        break
                    }
                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songSeekbarSb.progress = ((mills / playTime)*100).toInt()
                        }
                        if(mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch(e: InterruptedException){
                Log.d("SongActivity","쓰레드가 죽었습니다")
            }

        }

    }
    //사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songSeekbarSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(song)
        editor.putString("songData",songJson)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}