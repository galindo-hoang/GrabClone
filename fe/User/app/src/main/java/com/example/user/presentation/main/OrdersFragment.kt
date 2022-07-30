package com.example.user.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.databinding.FragmentOrdersBinding
import com.example.user.presentation.main.adapter.HistoryAdapter

class OrdersFragment: Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(layoutInflater,container,false)
        setRecyclerView()
        return binding.root
    }

    private fun setRecyclerView() {
        historyAdapter = HistoryAdapter()
        binding.rv.adapter = historyAdapter
        binding.rv.layoutManager = LinearLayoutManager(activity)
    }


}