package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var song:Song = Song()
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyApplication)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationView()

//        MainActivity에서 SongActivity로 데이터 값 넘기기 위한 변수명 지정
//        val song = Song(binding.mainPlayTitleTv.text.toString(), binding.mainPlaySingerTv.text.toString(),0,60,false,"music_lilac")

        //SongActivity에서 Mainactivity로 데이터 넘긴거 받고 가수명과 노래 제목 toast로 출력
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val message = data.getStringExtra("message")
                    Log.d("message", message!!)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        //MainActivity에서 SongActivity로 데이터 값 넘기는 코드
        binding.mainPlayCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer",song.singer)
            intent.putExtra("second",song.second)
            intent.putExtra("playTime",song.playTime)
            intent.putExtra("isPlaying",song.isPlaying)
            intent.putExtra("music",song.music)
            activityResultLauncher.launch(intent)
        }



        Log.d("Song",song.title + song.singer)

        if (savedInstanceState == null) {
            binding.mainBottomNavigation.selectedItemId = R.id.fragment_home
        }
    }

        fun setBottomNavigationView() {

            supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment()).commitAllowingStateLoss()
            binding.mainBottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.homeFragment -> {
                        supportFragmentManager.beginTransaction().replace(
                            R.id.main_container,
                            HomeFragment()
                        ).commit()
                        true
                    }

                    R.id.lookFragment -> {
                        supportFragmentManager.beginTransaction().replace(
                            R.id.main_container,
                            LookFragment()
                        ).commit()
                        true
                    }

                    R.id.searchFragment -> {
                        supportFragmentManager.beginTransaction().replace(
                            R.id.main_container,
                            SearchFragment()
                        ).commit()
                        true
                    }

                    R.id.lockerFragment -> {
                        supportFragmentManager.beginTransaction().replace(
                            R.id.main_container,
                            LockerFragment()
                        ).commit()
                        true
                    }

                    else -> false
                }
            }
        }
    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if(songJson == null) { // 최초 실행 시
            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
        } else { // SongActivity에서 노래가 한번이라도 pause 된 경우
            gson.fromJson(songJson, Song::class.java)
        }
        setMiniPlayer(song)
    }
    private fun setMiniPlayer(song: Song){
        binding.mainPlayTitleTv.text = song.title
        binding.mainPlaySingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second * 100000 / song.playTime)
    }
}

