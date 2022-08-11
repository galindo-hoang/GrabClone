package com.example.user.presentation.booking

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.data.dto.Payment
import com.example.user.data.dto.Vehicle
import com.example.user.databinding.ActivityMethodBookingBinding
import com.example.user.presentation.BaseActivity
import com.example.user.presentation.booking.adapter.PaymentAdapter
import com.example.user.presentation.booking.adapter.VehicleAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MethodBookingActivity : BaseActivity() {
    @Inject
    lateinit var vehicleAdapter: VehicleAdapter
    @Inject
    lateinit var paymentAdapter: PaymentAdapter
    private lateinit var binding: ActivityMethodBookingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMethodBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.rcvVehicle.adapter = vehicleAdapter
        binding.rcvVehicle.layoutManager = LinearLayoutManager(this)
        vehicleAdapter.setList(getDataV())
        // set up divider
        val dividerItemDecoration = MaterialDividerItemDecoration(
            binding.root.context, LinearLayoutManager.VERTICAL
        ).apply {
            this.isLastItemDecorated = false
        }
        binding.rcvVehicle.addItemDecoration(dividerItemDecoration)
        binding.rcvPayment.addItemDecoration(dividerItemDecoration)
        vehicleAdapter.setOnClickListener { it, position ->
        }
        binding.rcvPayment.adapter = paymentAdapter
        binding.rcvPayment.layoutManager = LinearLayoutManager(this)
        paymentAdapter.setList(getData())
        paymentAdapter.setOnClickListener { it, position ->
        }
        binding.rcvPayment.adapter = paymentAdapter
        binding.rcvPayment.layoutManager = LinearLayoutManager(this)
        paymentAdapter.setList(getData())
        paymentAdapter.setOnClickListener { it, position ->
            Log.e("---------",it.toString())
            Log.e("=========",position.toString())
        }
    }

    fun getDataV(): List<Vehicle>{
        val a = mutableListOf<Vehicle>()
        a.add(Vehicle(0))
        a.add(Vehicle(1))
        a.add(Vehicle(2))
        return a
    }
    fun getData(): List<Payment>{
        val a = mutableListOf<Payment>()
        a.add(Payment(0))
        a.add(Payment(1))
        a.add(Payment(2))
        return a
    }
}