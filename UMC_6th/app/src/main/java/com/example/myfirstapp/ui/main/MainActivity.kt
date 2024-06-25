package com.example.myfirstapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.R
import com.example.myfirstapp.ui.main.search.SearchFragment
import com.example.myfirstapp.ui.song.SongActivity
import com.example.myfirstapp.data.local.SongDatabase
import com.example.myfirstapp.data.entities.Album
import com.example.myfirstapp.data.entities.Song
import com.example.myfirstapp.databinding.ActivityMainBinding
import com.example.myfirstapp.ui.main.home.HomeFragment
import com.example.myfirstapp.ui.main.locker.LockerFragment
import com.example.myfirstapp.ui.main.look.LookFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>


    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyApplication)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationView()
        inputDummySongs()
        initPlayList()


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
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            activityResultLauncher.launch(intent)
        }


        if (savedInstanceState == null) {
            binding.mainBottomNavigation.selectedItemId = R.id.fragment_home
        }
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun getJwt() : String? {
        val spf = this.getSharedPreferences("auth2", MODE_PRIVATE)

        return spf!!.getString("jwt", "")
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
    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        setMiniPlayer(songs[nowPos])
    }

    private fun setMiniPlayer(song: Song){
        binding.mainPlayTitleTv.text = song.title
        binding.mainPlaySingerTv.text = song.singer
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val second = sharedPreferences.getInt("second", 0)
        Log.d("spfSecond", second.toString())
        binding.mainMiniplayerProgressSb.progress = (second * 100000 / song.playTime)
    }
    fun updateMainPlayerCl(album : Album) {
        binding.mainPlayTitleTv.text = album.title
        binding.mainPlaySingerTv.text = album.singer
        binding.mainMiniplayerProgressSb.progress = 0
    }
    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        // songs에 데이터가 이미 존재해 더미 데이터를 삽입할 필요가 없음
        if (songs.isNotEmpty()) return

        // songs에 데이터가 없을 때에는 더미 데이터를 삽입해주어야 함
        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                R.drawable.img_album_exp3,
                false,
                3
            )
        )


        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
                4
            )
        )


        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
                5
            )
        )

        // DB에 데이터가 잘 들어갔는지 확인
        val songDBData = songDB.songDao().getSongs()
        Log.d("DB data", songDBData.toString())
    }
}


