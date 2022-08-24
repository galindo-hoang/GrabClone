package com.example.user.presentation.booking

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.R
import com.example.user.data.api.AuthenticationApi
import com.example.user.data.dto.Payment
import com.example.user.data.dto.Vehicle
import com.example.user.databinding.ActivityMethodBookingBinding
import com.example.user.presentation.BaseActivity
import com.example.user.presentation.booking.adapter.PaymentAdapter
import com.example.user.presentation.booking.adapter.VehicleAdapter
import com.example.user.utils.PaymentMethod
import com.example.user.utils.Status
import com.example.user.utils.TypeCar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@SuppressLint("UseCompatLoadingForDrawables")
@AndroidEntryPoint
class MethodBookingActivity : BaseActivity() {
    @Inject
    lateinit var vehicleAdapter: VehicleAdapter
    @Inject
    lateinit var paymentAdapter: PaymentAdapter
    @Inject
    lateinit var bookingViewModel: BookingViewModel
    private lateinit var binding: ActivityMethodBookingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMethodBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerView()
        registerViewChange()
        registerLickListener()
    }

    private fun registerLickListener() {
        binding.btnSearchingCar.setOnClickListener {
            bookingViewModel.searchingDriver().observe(this) {
                when(it.status){
                    Status.LOADING -> this.showProgressDialog()
                    Status.ERROR -> {
                        this.hideProgressDialog()
                        if(it.responseCode == -2) this.showExpiredTokenDialog(it.message.toString())
                        else Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                    }
                    Status.SUCCESS -> {
                        this.hideProgressDialog()
                        bookingViewModel.isBooking = true
                        finish()
                    }
                }
            }
        }
        vehicleAdapter.setOnClickListener { it, _ -> bookingViewModel.vehicle = it }
        paymentAdapter.setOnClickListener { it, _ -> bookingViewModel.payment = it }
    }

    private fun registerViewChange() {
    }

    private fun setRecyclerView() {
        binding.rcvVehicle.adapter = vehicleAdapter
        binding.rcvVehicle.layoutManager = LinearLayoutManager(this)
        vehicleAdapter.setList(getTypeCarData())
        bookingViewModel.vehicle = vehicleAdapter.getItem()

        binding.rcvPayment.adapter = paymentAdapter
        binding.rcvPayment.layoutManager = LinearLayoutManager(this)
        paymentAdapter.setList(gePaymentData())
        bookingViewModel.payment = paymentAdapter.getItem()
    }

    private fun getTypeCarData(): List<Vehicle> = mutableListOf<Vehicle>().apply {
        this.add(Vehicle(
            TypeCar.CAR,
            getDrawable(R.drawable.ic_vehicle_car_40),
            bookingViewModel.distance!!
        ))
        this.add(Vehicle(
            TypeCar.MOTORCYCLE,
            getDrawable(R.drawable.ic_vehicle_motorcycle_40),
            bookingViewModel.distance!!
        ))
    }

    private fun gePaymentData(): List<Payment> = mutableListOf<Payment>().apply {
        this.add(Payment(PaymentMethod.CASH, getDrawable(R.drawable.ic_payment_cash_40)))
        this.add(Payment(PaymentMethod.CREDIT_CARD, getDrawable(R.drawable.ic_payment_credit_card_40)))
    }
}