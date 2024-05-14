package com.example.umc_6th

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_6th.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val albumTitle = result.data?.getStringExtra("albumTitle") ?: "No Title Provided"
                Toast.makeText(this, albumTitle, Toast.LENGTH_LONG).show()
                val elapsedSeconds = result.data?.getIntExtra("elapsedSeconds", 0) ?: 0
                updateSeekBar(elapsedSeconds)
            }
        }

        setBottomNavigationView()

        if(savedInstanceState == null){
            binding.mainBottomNavigation.selectedItemId = R.id.fragment_home
        }

        binding.layoutPlayContainer.setOnClickListener(){
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("songTitle", binding.txPlayTitle.text)
            intent.putExtra("songArtist", binding.txPlayArtist.text)
            resultLauncher.launch(intent)
        }

        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        binding.btnMainStart.setOnClickListener {
            if (binding.mainSeekBar.isEnabled) {
                binding.mainSeekBar.isEnabled = false
            } else {
                binding.mainSeekBar.isEnabled = true
            }
        }

        binding.layoutPlayContainer.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun updateSeekBar(elapsedSeconds: Int) {
        val max = binding.mainSeekBar.max
        val progress = elapsedSeconds * max / 60
        binding.mainSeekBar.progress = progress
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