package com.example.driver.presentation

import android.app.Dialog
import android.content.*
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.driver.R
import com.example.driver.data.model.booking.ReceiveNewBooking
import com.example.driver.databinding.DialogLoginBinding
import com.example.driver.databinding.DialogNewBookingBinding
import com.example.driver.domain.usecase.SetupServiceCurrentLocationUseCase
import com.example.driver.presentation.login.LogInActivity
import com.example.driver.presentation.main.StimulateActivity
import com.example.driver.presentation.main.StimulateViewModel
import com.example.driver.utils.Constant
import com.example.driver.utils.Status
import com.google.gson.Gson
import javax.inject.Inject

open class BaseActivity @Inject constructor(): AppCompatActivity() {

    @Inject
    lateinit var baseViewModel: BaseViewModel
    @Inject
    lateinit var setupServiceCurrentLocationUseCase: SetupServiceCurrentLocationUseCase
    @Inject
    lateinit var stimulateViewModel: StimulateViewModel

    private var mProgressDialog: Dialog? = null
    private var mExpiredTokenDialog: Dialog? = null
    private var mNewBookingDialog: Dialog? = null

    private var haveNewBooking: BroadcastReceiver? = null
    private fun showNewBookingDialog(receiveNewBooking: ReceiveNewBooking){
        this.mNewBookingDialog = Dialog(this)
        val dialogNewBookingBinding = DialogNewBookingBinding.inflate(LayoutInflater.from(this))
        this.mNewBookingDialog?.setContentView(dialogNewBookingBinding.root)
        dialogNewBookingBinding.tvNewBookingDes.text = Constant.convertLatLongToAddress(this, receiveNewBooking.origin.convertToLatLng())
        dialogNewBookingBinding.tvNewBookingSrc.text = Constant.convertLatLongToAddress(this, receiveNewBooking.destination.convertToLatLng())
        dialogNewBookingBinding.btnNewBookingAccept.setOnClickListener {
            stimulateViewModel.acceptBooking(receiveNewBooking.bookingId).observe(this) {
                this.mNewBookingDialog?.dismiss()
                when(it.status) {
                    Status.LOADING -> this.showProgressDialog()
                    Status.SUCCESS -> {
                        stopLooking()
                        stimulateViewModel.origin = receiveNewBooking.origin
                        stimulateViewModel.destination = receiveNewBooking.destination
                        this.hideProgressDialog()
                        startActivity(Intent(this,StimulateActivity::class.java))
                    }
                    Status.ERROR -> {
                        this.hideProgressDialog()
                        if(it.codeResponse == -2) this.showExpiredTokenDialog(it.message.toString())
                        else Toast.makeText(this,it.message.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        dialogNewBookingBinding.btnNewBookingCancel.setOnClickListener {
            this.mNewBookingDialog?.dismiss()
        }
        this.mNewBookingDialog?.show()
    }
    fun startLooking() {
        if(haveNewBooking == null) {
            haveNewBooking = object : BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    this@BaseActivity.showNewBookingDialog(
                        Gson().fromJson(p1!!.getStringExtra(Constant.HAVE_NEW_BOOKING_EXTRA), ReceiveNewBooking::class.java)
                    )
                }
            }
            setupServiceCurrentLocationUseCase.start(application)
            registerReceiver(haveNewBooking,IntentFilter(Constant.HAVE_NEW_BOOKING))
        }else {
            Toast.makeText(this,"Is Listening passenger",Toast.LENGTH_LONG).show()
        }
    }
    fun stopLooking(){
        if(haveNewBooking != null) {
            setupServiceCurrentLocationUseCase.stop()
            unregisterReceiver(haveNewBooking)
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    fun showExpiredTokenDialog(userName: String){
        this.mExpiredTokenDialog = Dialog(this)
        val dialogLoginBinding = DialogLoginBinding.inflate(LayoutInflater.from(this))
        mExpiredTokenDialog?.setContentView(dialogLoginBinding.root)
        baseViewModel.userName = userName
        dialogLoginBinding.btnReLoginAccept.setOnClickListener {
            val password = dialogLoginBinding.etReLogin.text.toString()
            if(password.isEmpty()) Toast.makeText(this,"Please write password",Toast.LENGTH_LONG).show()
            else {
                baseViewModel.password = password
                baseViewModel.login(BaseApplication.token).observe(this) {
                    when(it.status) {
                        Status.LOADING -> this.showProgressDialog()
                        Status.SUCCESS -> {
                            baseViewModel.password = null
                            baseViewModel.userName = null
                            this.hideProgressDialog()
                            this.mExpiredTokenDialog?.dismiss()
                        }
                        Status.ERROR -> {
                            this.hideProgressDialog()
                            when(it.codeResponse) {
                                -1 -> {
                                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                                    this.mExpiredTokenDialog?.dismiss()
                                    baseViewModel.logout()
                                    finishAffinity()
                                    startActivity(Intent(this, LogInActivity::class.java))
                                }
                                401 -> Toast.makeText(this,"Unauthorized",Toast.LENGTH_LONG).show()
                                else -> Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
        dialogLoginBinding.btnReLoginCancel.setOnClickListener {
            this.mExpiredTokenDialog?.dismiss()
            baseViewModel.logout()
            finishAffinity()
            startActivity(Intent(this, LogInActivity::class.java))
        }
        this.mExpiredTokenDialog?.show()
    }

    fun showProgressDialog(text: String = "Please waiting...") {
        this.mProgressDialog = Dialog(this)
        mProgressDialog?.setContentView(R.layout.dialog_progress)
        mProgressDialog?.findViewById<TextView>(R.id.tvProgress)?.text = text
        mProgressDialog?.show()
    }

    fun hideProgressDialog() { mProgressDialog?.dismiss() }


    override fun onStart() {
        super.onStart()
        registerReceiver(listeningRefreshToken, IntentFilter(Constant.REFRESH_TOKEN_EXPIRED_WHEN_SEND_LOCATION))
    }

    override fun onPause() {
        unregisterReceiver(listeningRefreshToken)
        super.onPause()
    }

    private val listeningRefreshToken: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            val userName = p1?.getStringExtra(Constant.REFRESH_TOKEN_EXPIRED_WHEN_SEND_LOCATION_EXTRA_USERNAME)
            if(userName?.isNotEmpty() == true){
                this@BaseActivity.showExpiredTokenDialog(userName)
            }
        }

    }
}