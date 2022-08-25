package com.example.driver.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.driver.R
import com.example.driver.databinding.ActivityMainBinding
import com.example.driver.presentation.BaseActivity
import com.example.driver.presentation.service.MyFirebaseMessaging
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(stimulateViewModel.afterDoneDriving) {
            this.startLooking()
            stimulateViewModel.afterDoneDriving = false
        }
        loadFragment(HomeFragment())
        registerViewChangeListener()
        registerClickListener()
    }

    private fun registerClickListener() {}

    private fun registerViewChangeListener() {
        binding.bnv.setOnItemSelectedListener { items ->
            when(items.itemId){
                R.id.bnv_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.bnv_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainerMain.id,fragment)
            .commit()
    }
}