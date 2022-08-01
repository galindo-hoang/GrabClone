package com.example.user.presentation.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.R
import com.example.user.data.dto.Vehicle
import com.example.user.databinding.ActivityMethodBookingBinding
import com.example.user.presentation.BaseActivity
import com.example.user.presentation.booking.adapter.VehicleAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MethodBookingActivity : BaseActivity() {
    @Inject
    lateinit var vehicleAdapter: VehicleAdapter
    private lateinit var binding: ActivityMethodBookingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMethodBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.rcv.adapter = vehicleAdapter
        binding.rcv.layoutManager = LinearLayoutManager(this)
        vehicleAdapter.setList(getData())
        vehicleAdapter.setOnClickListener { it, position ->
            Log.e("---------",it.toString())
            Log.e("=========",position.toString())
        }
    }

    fun getData(): List<Vehicle>{
        val a = mutableListOf<Vehicle>()
        a.add(Vehicle(0))
        a.add(Vehicle(0))
        a.add(Vehicle(0))
        a.add(Vehicle(0))
        a.add(Vehicle(0))
        a.add(Vehicle(0))
        a.add(Vehicle(0))
        return a
    }
}