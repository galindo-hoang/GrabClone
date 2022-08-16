package com.example.user.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.user.data.api.BookingApi
import com.example.user.databinding.FragmentHomeBinding
import com.example.user.presentation.main.adapter.BookingAdapter
import com.example.user.presentation.main.adapter.PromptAdapter
import com.example.user.presentation.main.adapter.VoucherAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
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
    lateinit var bookingViewModel: BookingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
//        setRecycleView()
//        registerClickListener()
//        registerViewChangeListener()
        binding.btn.setOnClickListener {
            bookingViewModel.searchingDriver()
        }
        return binding.root
    }

//    private fun registerViewChangeListener() {
//        binding.selector1.setOnClickListener {
//            startActivity(Intent(activity,SearchingActivity::class.java))
//        }
//        binding.selector2.setOnClickListener {
//            startActivity(Intent(activity,SearchingActivity::class.java))
//        }
//    }

    private fun registerClickListener() {}

//    private fun setRecycleView() {
//        bookingAdapter = BookingAdapter(listOf())
//        binding.rvBookingNow.adapter = bookingAdapter
//        binding.rvBookingNow.layoutManager = LinearLayoutManager(activity)
//        promptAdapter = PromptAdapter(listOf())
//        binding.rvPrompt.adapter = promptAdapter
//        binding.rvPrompt.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
//        voucherAdapter = VoucherAdapter(listOf())
//        binding.rvVoucher.adapter = voucherAdapter
//        binding.rvVoucher.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
//    }
}