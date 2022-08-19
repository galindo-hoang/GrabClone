package com.example.user.presentation.booking

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.data.api.AuthenticationApi
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
    @Inject
    lateinit var authenticationApi: AuthenticationApi
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
        val dividerItemDecoration = MaterialDividerItemDecoration(
            binding.root.context, LinearLayoutManager.VERTICAL
        ).apply {
            this.isLastItemDecorated = false
        }
        binding.rcvVehicle.addItemDecoration(dividerItemDecoration)
        binding.rcvPayment.addItemDecoration(dividerItemDecoration)
        vehicleAdapter.setOnClickListener { it, position ->
            Log.e("---------",it.toString())
            Log.e("=========",position.toString())
        }
        binding.rcvPayment.adapter = paymentAdapter
        binding.rcvPayment.layoutManager = LinearLayoutManager(this)
        paymentAdapter.setList(gePaymentData())
        paymentAdapter.setOnClickListener { it, position ->
            Log.e("---------",it.toString())
            Log.e("=========",position.toString())
        }
    }

    private fun getDataV(): List<Vehicle>{
        val a = mutableListOf<Vehicle>()
        a.add(Vehicle(0))
        a.add(Vehicle(0))
        a.add(Vehicle(0))
        return a
    }
    private fun gePaymentData(): List<Payment>{
        val a = mutableListOf<Payment>()
        a.add(Payment(0))
        a.add(Payment(0))
        a.add(Payment(0))
        return a
    }
}