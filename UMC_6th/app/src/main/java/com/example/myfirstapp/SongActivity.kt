package com.example.myfirstapp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myfirstapp.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()


    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }
    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songMiniplayerPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songMiniplayerNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.songMiniplayerPreIv.setOnClickListener {
            moveSong(-1)
        }
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }
    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
        }
        nowPos +=direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }
    // songId로 position을 얻는 메서드
    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }
    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if (!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

    }

    private fun setPlayer(song : Song) {
        binding.songMusicTitleTv.text = song.title
        binding.songMusicSingerTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songSeekbarSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }
    fun setPlayerStatus (isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){ // 재생중
            binding.songMiniplayerPauseIv.visibility = View.GONE
            binding.songMiniplayerPlayIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else { // 일시정지
            binding.songMiniplayerPauseIv.visibility = View.VISIBLE
            binding.songMiniplayerPlayIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true) { // 재생 중이 아닐 때에 pause를 하면 에러가 나기 때문에 이를 방지
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
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
        songs[nowPos].second = (songs[nowPos].playTime * binding.songSeekbarSb.progress) / 100000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("second", songs[nowPos].second)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}