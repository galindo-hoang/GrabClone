package com.example.driver.presentation

import android.app.Dialog
import android.content.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.driver.R
import com.example.driver.domain.usecase.SetupServiceCurrentLocationUseCase
import com.example.driver.exception.ExpiredRefreshTokenExceptionCustom
import com.example.driver.presentation.login.LogInActivity
import com.example.driver.presentation.main.StimulateActivity
import com.example.driver.presentation.main.StimulateViewModel
import com.example.driver.utils.Constant
import com.example.driver.utils.Status
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

open class BaseActivity @Inject constructor(): AppCompatActivity() {

    @Inject
    lateinit var baseViewModel: BaseViewModel
    @Inject
    lateinit var setupServiceCurrentLocationUseCase: SetupServiceCurrentLocationUseCase
    @Inject
    lateinit var stimulateViewModel: StimulateViewModel

    private lateinit var mProgressDialog: Dialog
    private lateinit var mExpiredTokenDialog: Dialog
    private lateinit var mNewBookingDialog: Dialog

    fun startLooking(){
        stimulateViewModel.haveBooking.observe(this) {
            if(it) {
                this.showNewBookingDialog()
            }
        }
    }



    fun stopLooking(){
        stimulateViewModel.haveBooking.removeObservers(this)
    }

    private fun registerViewChangeExpired() {
        baseViewModel.isLogin.observe(this) {
            when(it){
                1 -> {
                    baseViewModel.password = null
                    baseViewModel.userName = null
                    hideProgressDialog()
                    hideExpiredTokenDialog()
                }
                -1 -> {
                    hideProgressDialog()
                    Toast.makeText(this, "password wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun registerClickListenerExpired() {
        this.mExpiredTokenDialog.findViewById<AppCompatButton>(R.id.btn_re_login_accept).setOnClickListener {
            val password = this@BaseActivity.mExpiredTokenDialog.findViewById<AppCompatEditText>(R.id.et_re_login).text.toString()
            if(password.isEmpty())
                Toast.makeText(this,"Please write password",Toast.LENGTH_LONG).show()
            else {
                baseViewModel.password = password
                baseViewModel.login()
                this@BaseActivity.showProgressDialog("Please waiting...")
            }
        }

        this.mExpiredTokenDialog.findViewById<AppCompatButton>(R.id.btn_re_login_cancel).setOnClickListener {
            this.mExpiredTokenDialog.dismiss()
            baseViewModel.logout()
            finishAffinity()
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }

    fun showNewBookingDialog(){
        this.mNewBookingDialog = Dialog(this)
        mNewBookingDialog.setContentView(R.layout.dialog_new_booking)
        this.mNewBookingDialog.findViewById<TextView>(R.id.tv_new_booking_des).text =
            Constant.convertLatLongToAddress(
                this,
                LatLng(stimulateViewModel.destination!!.lat,stimulateViewModel.destination!!.long)
            )
        this.mNewBookingDialog.findViewById<TextView>(R.id.tv_new_booking_src).text =
            Constant.convertLatLongToAddress(
                this,
                LatLng(stimulateViewModel.source!!.lat,stimulateViewModel.source!!.long)
            )
        registerClickListenerNewBooking()
        registerViewChangeNewBooking()
        this.mNewBookingDialog.show()
    }

    private fun registerClickListenerNewBooking() {
        this.mNewBookingDialog.findViewById<AppCompatButton>(R.id.btn_new_booking_accept).setOnClickListener {
            try {
                stimulateViewModel.acceptBooking().observe(this) {
                    this.mNewBookingDialog.dismiss()
                    when(it.status){
                        Status.SUCCESS -> {
                            if(it.data == 1) {
                                setupServiceCurrentLocationUseCase.stop()
                                startActivity(Intent(this, StimulateActivity::class.java))
                            }
                        }
                        else -> {}
                    }
                }
            } catch (e:ExpiredRefreshTokenExceptionCustom){
                this.showExpiredTokenDialog(e.message.toString())
            }
        }
        this.mNewBookingDialog.findViewById<AppCompatButton>(R.id.btn_new_booking_cancel).setOnClickListener {
            this.mNewBookingDialog.dismiss()
        }
    }

    private fun registerViewChangeNewBooking() {}

    fun showExpiredTokenDialog(userName: String){
        this.mExpiredTokenDialog = Dialog(this)
        mExpiredTokenDialog.setContentView(R.layout.dialog_login)
        baseViewModel.userName = userName
        registerClickListenerExpired()
        registerViewChangeExpired()
        this.mExpiredTokenDialog.show()
    }

    private fun hideExpiredTokenDialog() {
        baseViewModel.isLogin.removeObservers(this)
        mExpiredTokenDialog.dismiss()
    }

    fun showProgressDialog(text: String) {
        this.mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.findViewById<TextView>(R.id.tvProgress).text = text
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }


    override fun onStart() {
        super.onStart()
        registerReceiver(listeningRefreshToken, IntentFilter(Constant.REFRESH_TOKEN_EXPIRED))
    }

    override fun onPause() {
        unregisterReceiver(listeningRefreshToken)
        super.onPause()
    }

    private val listeningRefreshToken: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            val userName = p1?.getStringExtra(Constant.REFRESH_TOKEN_EXPIRED_PUT_EXTRA_USERNAME)
            if(userName?.isNotEmpty() == true){
                this@BaseActivity.showExpiredTokenDialog(userName)
            }
        }

    }
}