package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myfirstapp.databinding.ActivityMainBinding
import com.example.myfirstapp.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    private var isColorChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var title : String? = null
        var singer : String? = null

        //MainActivity에서 SongActivity로 넘긴 데이터 값 받는 곳
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            title = intent.getStringExtra("title")
            singer = intent.getStringExtra("singer")
            binding.songMusicTitleTv.text = title
            binding.songMusicSingerTv.text = singer
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
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songMusicSingerTv.text = intent.getStringExtra("singer")
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

//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtra("message", "뒤로가기 버튼 클릭")
//        setResult(RESULT_OK, intent)
//        finish()
//    }

    fun setPlayerStatus(isPlaying : Boolean){
        if(isPlaying){ //재생중
            binding.songMiniplayerPlayIv.visibility = View.VISIBLE
            binding.songMiniplayerPauseIv.visibility = View.GONE
        }
        else{ //일시정지
            binding.songMiniplayerPlayIv.visibility = View.GONE
            binding.songMiniplayerPauseIv.visibility = View.VISIBLE
        }
    }
}