package com.example.umc_6th

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.umc_6th.adapter.SharedPreferencesHelper
import com.example.umc_6th.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: HomeFragment.SharedViewModel

    private var song:Song = Song()
    private val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    private var nowPos = 0
    private var gson: Gson = Gson()
    private var isPlaying: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val prefs = getSharedPreferences("song", MODE_PRIVATE)

        setContentView(binding.root)

        inputDummySongs()
 //       inputDummyAlbums()
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val message = data.getStringExtra("message")
                    Log.d("message", message!!)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        setBottomNavigationView()

        if(savedInstanceState == null){
            binding.mainBottomNavigation.selectedItemId = R.id.fragment_home
        }
/*
        binding.layoutPlayContainer.setOnClickListener(){
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("songTitle", binding.txPlayTitle.text.toString())
            intent.putExtra("songArtist", binding.txPlayArtist.text.toString())
            resultLauncher.launch(intent)
        }

 */

        initPlayList()
        setupButtonListeners()
        //아래는 뭐지??
        viewModel = ViewModelProvider(this).get(HomeFragment.SharedViewModel::class.java)
        viewModel.selectedTitle.observe(this, Observer { title ->
            binding.txPlayTitle.text = title
        })

        viewModel.selectedArtist.observe(this, Observer { artist ->
            binding.txPlayArtist.text = artist
        })

        binding.layoutPlayContainer.setOnClickListener{
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId",songs[nowPos].id-1)
            editor.apply()

            val intent = Intent(this,SongActivity::class.java)
            startActivity(intent)
        }

        Log.d("MainActivity", getJwt().toString())
    }

    private fun getJwt() : String? {
        val spf = this.getSharedPreferences("auth", MODE_PRIVATE)

        return spf!!.getString("jwt", "")
    }
//문제없음..
    override fun onStart() {
        super.onStart()
        val prefs = getSharedPreferences("song", MODE_PRIVATE)
        val songId = prefs.getInt("songId",0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0){
            songDB.songDao().getSong(1)
        } else{
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }


    override fun onPause() {
        super.onPause()
        savePlaybackState(isPlaying)  // 현재 재생 상태를 저장
    }

    override fun onResume() {
        super.onResume()
        updateFromSharedPrefs()  // 추가적으로 UI 업데이트
        Log.d("MainActivityNextSongId", nowPos.toString())
    }


    private fun setupButtonListeners() {
        binding.btnMainStart.setOnClickListener {
            isPlaying = !isPlaying
            updatePlaybackUI(isPlaying)  // 재생 버튼 상태 업데이트 등
            savePlaybackState(isPlaying)  // 재생 상태 저장
        }

        binding.btnMainStart.setOnClickListener {
            if (binding.mainSeekBar.isEnabled) {
                binding.mainSeekBar.isEnabled = false
            } else {
                binding.mainSeekBar.isEnabled = true
            }
        }
        binding.btnMainNext.setOnClickListener {
            playNextSong()
        }
        binding.btnMainPrev.setOnClickListener {
            playPreviousSong()
        }
    }

    private fun updatePlaybackUI(isPlaying: Boolean) {
        if (isPlaying) {
            binding.btnMainStart.setImageResource(R.drawable.btn_miniplay_pause) // 재생 중 아이콘으로 변경
        } else {
            binding.btnMainStart.setImageResource(R.drawable.btn_miniplayer_play) // 일시 정지 아이콘으로 변경
        }
    }

    private fun updateSeekBar(elapsedSeconds: Int, totalTime: Int) {
        val max = binding.mainSeekBar.max
        val progress = elapsedSeconds * max / totalTime
        binding.mainSeekBar.progress = progress
    }

    private fun setMiniPlayer(song : Song){
        runOnUiThread {
            binding.txPlayTitle.text = song.title
            binding.txPlayArtist.text = song.artist
            binding.mainSeekBar.progress = (song.second * 100000) / song.playTime
        }
    }

    private fun playNextSong() {
        val prefs = getSharedPreferences("song", MODE_PRIVATE)
        val songDB = SongDatabase.getInstance(this)!!
        val currentSongId = prefs.getInt("songId",0)
        val nextSong = songDB.songDao().getNextSong(currentSongId)  // 다음 곡을 조회하는 로직 필요
        if (nextSong != null) {
            setMiniPlayer(nextSong)
            val editor = prefs.edit()
            editor.putInt("songId", nextSong.id)
            editor.apply()
            Log.d("isRight?", nextSong.id.toString())
        // 일단 재생중으로 선택. Shared Preferences에 다음 곡 ID 저장
        }
    }

    private fun playPreviousSong() {
        val prefs = getSharedPreferences("song", MODE_PRIVATE)
        val songDB = SongDatabase.getInstance(this)!!
        val currentSongId = prefs.getInt("songId",0)
        val previousSong = songDB.songDao().getPreviousSong(currentSongId)  // 이전 곡을 조회하는 로직 필요
        if (previousSong != null) {
            setMiniPlayer(previousSong)
            val editor = prefs.edit()
            editor.putInt("songId", previousSong.id)
            editor.apply()
            Log.d("isRight?", previousSong.id.toString())
        // 일단 재생중으로 선택. Shared Preferences에 이전 곡 ID 저장
        }
    }

    private fun updateFromSharedPrefs() {
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPref.getInt("songId", -1)
        nowPos = getPlayingSongPosition(songId)
        if (songId != nowPos) {
            val songTitle = sharedPref.getString("songTitle", "")
            val songArtist = sharedPref.getString("songArtist", "")
            val songProgress = sharedPref.getInt("songProgress", 0)

            Log.d("MainActivity", "Loaded songId: $songId, Title: $songTitle, Artist: $songArtist, Progress: $songProgress")
            binding.txPlayTitle.text = songTitle
            binding.txPlayArtist.text = songArtist
            updateSeekBar(songProgress, songs[nowPos].playTime)
        }
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun savePlaybackState(isPlaying: Boolean) {
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean("isPlaying", isPlaying)
            putString("songTitle", binding.txPlayTitle.text.toString())
            putString("songArtist", binding.txPlayArtist.text.toString())
            putInt("songProgress", binding.mainSeekBar.progress)
            apply()
        }
    }

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()
        if(songs.isNotEmpty()) return
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
                "방탄소년단 (BTS)",
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

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }

    private fun setBottomNavigationView() {
        binding.mainBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_container,
                        HomeFragment()
                    ).commit()
                    true
                }
                R.id.fragment_look -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_container,
                        LookFragment()
                    ).commit()
                    true
                }
                R.id.fragment_search -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_container,
                        SearchFragment()
                    ).commit()

                    true
                }
                R.id.fragment_locker -> {
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

}