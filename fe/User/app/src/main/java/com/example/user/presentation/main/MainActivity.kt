package com.example.user.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.user.R
import com.example.user.databinding.ActivityMainBinding
import com.example.user.presentation.base.BaseActivity
import com.example.user.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if(intent.hasExtra(Constant.CONGRATULATE)) {
            Toast.makeText(this,"Your booking is finish",Toast.LENGTH_LONG).show()
        }
        setContentView(binding.root)
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
                R.id.bnv_orders -> {
                    loadFragment(OrdersFragment())
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