package com.example.umc_6th

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.umc_6th.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: HomeFragment.SharedViewModel

    private var song:Song = Song()
    private var gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        inputDummySongs()
 //       inputDummyAlbums()
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val elapsedSeconds = result.data?.getIntExtra("elapsedSeconds", 0) ?: 0
                updateSeekBar(elapsedSeconds)
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

        setupButtonListeners()
        viewModel = ViewModelProvider(this).get(HomeFragment.SharedViewModel::class.java)
        viewModel.selectedTitle.observe(this, Observer { title ->
            binding.txPlayTitle.text = title
        })

        viewModel.selectedArtist.observe(this, Observer { artist ->
            binding.txPlayArtist.text = artist
        })

        binding.layoutPlayContainer.setOnClickListener{
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId",song.id)
            editor.apply()

            val intent = Intent(this,SongActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0){
            songDB.songDao().getSong(1)
        } else{
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }

    private fun setupButtonListeners() {
        binding.btnMainStart.setOnClickListener {
            if (binding.mainSeekBar.isEnabled) {
                binding.mainSeekBar.isEnabled = false
            } else {
                binding.mainSeekBar.isEnabled = true
            }
        }
    }

    private fun updateSeekBar(elapsedSeconds: Int) {
        val max = binding.mainSeekBar.max
        val progress = elapsedSeconds * max / 60
        binding.mainSeekBar.progress = progress
    }

    private fun setMiniPlayer(song : Song){
        binding.txPlayTitle.text = song.title
        binding.txPlayArtist.text = song.artist
        binding.mainSeekBar.progress = (song.second*100000)/song.playTime
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
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }
/*
    //ROOM_DB
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                0,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                1,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
            )
        )

    }

*/

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