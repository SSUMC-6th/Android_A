package com.example.umc_6th

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_6th.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.util.Timer

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
//    lateinit var song: Song
//    private var gson : Gson = Gson()

    lateinit var timer : Timer
    private var mediaPlayer : MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater) // binding 초기화
        setContentView(binding.root)

        binding.songMiniplayerIv.setOnClickListener{
            setPlayerStatus(true)
            startStopService()
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
            startStopService()
        }

        initPlayList()
        initSong()
//        setPlayer(song)
        initClickListener()

        var title : String? = null
        var singer : String? = null

//        if(intent.hasExtra("title") && intent.hasExtra("singer")){
//            title = intent.getStringExtra("title")
//            singer = intent.getStringExtra("singer")
//            binding.songMusicTitleTv.text = title
//            binding.songSingerNameTv.text = singer
//        }
    }

    private fun isServiceRunning(inputClass: Class<ForegroundService>): Boolean {
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

    private fun startStopService() {
        if(isServiceRunning(ForegroundService::class.java)){
            Toast.makeText(this, "Foreground Service Stopped",Toast.LENGTH_SHORT).show()
            stopService(Intent(this,ForegroundService::class.java))
        } else{
            Toast.makeText(this, "Foreground Service Started", Toast.LENGTH_SHORT).show()
            startService(Intent(this, ForegroundService::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        songs[nowPos].second = (songs[nowPos].playTime * binding.songProgressSb.progress) / 100000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songsId",songs[nowPos].id)
        //재생 중이던 song이 몇초까지 재생되었는지 정보 저장
        editor.putInt("second",songs[nowPos].second)
        editor.apply()
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtra("message", "뒤로가기 버튼 클릭")
//
//        setResult(RESULT_OK, intent)
//        finish()
//    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스를 해제한다.
        mediaPlayer = null // 미디어 플레이어를 해제한다.
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener{
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("message", title + " _ " + singer)
//            setResult(RESULT_OK, intent)
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener{
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(false)
        }
        binding.songNextIv.setOnClickListener{
            moveSong(+1)
        }
        binding.songPreviousIv.setOnClickListener{
            moveSong(-1)
        }
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun initSong(){
//        if(intent.hasExtra("title") && intent.hasExtra("singer")){
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("singer")!!,
//                intent.getIntExtra("second",0),
//                intent.getIntExtra("playTime",0),
//                intent.getBooleanExtra("isPlaying",false),
//                intent.getStringExtra("music")!!
//            )
//        }
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId",0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID",songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if(!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            Toast.makeText(this,"Liked",Toast.LENGTH_SHORT).show()
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            Toast.makeText(this,"Unliked",Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
//            nowPos = songs.size -1
            CustomSnackbar.make(binding.root, "처음 곡입니다.").show()
//            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
        }
        else if(nowPos + direct >= songs.size){
            CustomSnackbar.make(binding.root,"마지막 곡입니다.").show()
//            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
        }
        else {
            nowPos += direct

            timer.interrupt()
            startTimer()
            mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스를 해제한다.
            mediaPlayer = null // 미디어 플레이어를 해제한다.

            setPlayer(songs[nowPos])
        }
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second / 60,song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime / 60,song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)

        if(song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){ // 재생중
            binding.songMiniplayerIv.visibility= View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else{ // 일시정지
            binding.songMiniplayerIv.visibility=View.VISIBLE
            binding.songPauseIv.visibility = View.GONE

            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime , songs[nowPos].isPlaying)
        timer.start()
    }
    inner class Timer(private val playTime : Int, var isPlaying: Boolean = true) : Thread(){

        private var second : Int = 0
        private var mills : Float = 0f

        override fun run() {
            super.run()
            try {
                while (true) {
                    if (second >= playTime) {
                        break
                    }

                    while (!isPlaying) {
                        sleep(200)
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }
                        if (mills % 1000 == 0F) {
                            runOnUiThread {
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }
}