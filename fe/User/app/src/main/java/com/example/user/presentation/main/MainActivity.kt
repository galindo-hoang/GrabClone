package com.example.user.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.R
import com.example.user.databinding.ActivityMainBinding
import com.example.user.presentation.BaseActivity
import com.example.user.presentation.main.adapter.BookingAdapter
import com.example.user.presentation.main.adapter.PromptAdapter
import com.example.user.presentation.main.adapter.VoucherAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var bookingAdapter: BookingAdapter
    private lateinit var promptAdapter: PromptAdapter
    private lateinit var voucherAdapter: VoucherAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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