package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myfirstapp.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    private var isColorChanged = false
    lateinit var song : Song
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        var title : String? = null
        var singer : String? = null

        //MainActivity에서 SongActivity로 넘긴 데이터 값 받는 곳
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songMusicSingerTv.text = intent.getStringExtra("singer")
        }

        //SongActivity에서 Mainactivity로 데이터 넘기는 곳
        //songDownIb를 누르면 intent가 넘어감
        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("message", title + " _ " + singer)
            setResult(RESULT_OK, intent)
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

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }
    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false)
            )
        }
        startTimer()
    }
    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")
        binding.songMusicSingerTv.text = intent.getStringExtra("singer")
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)
        binding.songSeekbarSb.progress = (song.second *1000 /song.playTime)

        setPlayerStatus(song.isPlaying)
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtra("message", "뒤로가기 버튼 클릭")
//        setResult(RESULT_OK, intent)
//        finish()
//    }

    fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying
        if(isPlaying){ //재생중
            binding.songMiniplayerPlayIv.visibility = View.VISIBLE
            binding.songMiniplayerPauseIv.visibility = View.GONE
        }
        else{ //일시정지
            binding.songMiniplayerPlayIv.visibility = View.GONE
            binding.songMiniplayerPauseIv.visibility = View.VISIBLE
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
}