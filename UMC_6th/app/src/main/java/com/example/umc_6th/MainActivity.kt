package com.example.umc_6th

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.umc_6th.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationView()

        if(savedInstanceState == null){
            binding.mainBottomNavigation.selectedItemId = R.id.fragment_home
        }
    }

    fun setBottomNavigationView() {
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