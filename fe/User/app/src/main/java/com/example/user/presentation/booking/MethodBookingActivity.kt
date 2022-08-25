package com.example.user.presentation.booking

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.R
import com.example.user.data.dto.Payment
import com.example.user.data.dto.Vehicle
import com.example.user.databinding.ActivityMethodBookingBinding
import com.example.user.presentation.base.BaseActivity
import com.example.user.presentation.booking.adapter.PaymentAdapter
import com.example.user.presentation.booking.adapter.VehicleAdapter
import com.example.user.presentation.service.MyFirebaseMessaging
import com.example.user.utils.Constant
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
                        MyFirebaseMessaging.isWaiting = true
                        isFinding()
                    }
                }
            }
        }
        vehicleAdapter.setOnClickListener { it, _ -> bookingViewModel.vehicle = it }
        paymentAdapter.setOnClickListener { it, _ -> bookingViewModel.payment = it }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    var isCount: Int = 0
    private var waitingDriverLooper = object : Runnable {
            override fun run() {
                if(isCount < 10){
                    isCount += 1
                    waitingHandler.postDelayed(this, 1000)
                }else {
                    waitingHandler.removeCallbacks(this)
                    sendBroadcast(Intent(Constant.HAVE_DRIVER).apply { this.putExtra(Constant.HAVE_DRIVER_STRING, "NOT_HAVING") })
                }
            }
        }

    private val listeningDriver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            binding.loading.visibility = View.GONE
            unregisterReceiver(this)
            MyFirebaseMessaging.isWaiting = false
            if(p1?.getStringExtra(Constant.HAVE_DRIVER_STRING) == "HAVING"){
                waitingHandler.removeCallbacks(waitingDriverLooper)
                Toast.makeText(this@MethodBookingActivity,"Having driver accept your booking",Toast.LENGTH_LONG).show()
                bookingViewModel.routesForRouting = bookingViewModel.routes.value!!.data!!
                finishAffinity()
                startActivity(Intent(this@MethodBookingActivity,RoutingActivity::class.java))
            } else Toast.makeText(this@MethodBookingActivity,"Don't have driver",Toast.LENGTH_LONG).show()
        }
    }
    private var waitingHandler: Handler = Handler(Looper.myLooper()!!)
    private fun isFinding() {
        binding.loading.visibility = View.VISIBLE
        isCount = 0
        registerReceiver(listeningDriver, IntentFilter(Constant.HAVE_DRIVER))
        waitingHandler.post(waitingDriverLooper)
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
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