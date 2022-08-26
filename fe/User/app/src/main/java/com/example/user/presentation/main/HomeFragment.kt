package com.example.user.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.data.api.BookingApi
import com.example.user.data.dto.BookingDto
import com.example.user.databinding.FragmentHomeBinding
import com.example.user.presentation.base.BaseApplication
import com.example.user.presentation.booking.SearchingRouteActivity
import com.example.user.presentation.main.adapter.BookingAdapter
import com.example.user.presentation.main.adapter.PromptAdapter
import com.example.user.presentation.main.adapter.VoucherAdapter
import com.example.user.presentation.service.MyFirebaseMessaging
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var bookingAdapter: BookingAdapter
    private lateinit var promptAdapter: PromptAdapter
    private lateinit var voucherAdapter: VoucherAdapter
    @Inject
    lateinit var bookingApi: BookingApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        setRecycleView()
        registerClickListener()
        return binding.root
    }
    private fun registerClickListener() {
        binding.selector1.setOnClickListener {
            startActivity(Intent(activity,SearchingRouteActivity::class.java))
        }
    }

    private fun setRecycleView() {
        bookingAdapter = BookingAdapter(listOf())
        binding.rvBookingNow.adapter = bookingAdapter
        binding.rvBookingNow.layoutManager = LinearLayoutManager(activity)
        promptAdapter = PromptAdapter(listOf())
        binding.rvPrompt.adapter = promptAdapter
        binding.rvPrompt.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        voucherAdapter = VoucherAdapter(listOf())
        binding.rvVoucher.adapter = voucherAdapter
        binding.rvVoucher.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
    }
}