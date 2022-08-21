package com.example.user.presentation

import android.app.Dialog
import android.content.*
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.user.R
import com.example.user.presentation.login.LogInActivity
import com.example.user.utils.Constant
import javax.inject.Inject

open class BaseActivity @Inject constructor(): AppCompatActivity() {
    @Inject
    lateinit var baseViewModel: BaseViewModel

    private var mProgressDialog: Dialog? = null
    private var mExpiredTokenDialog: Dialog? = null

    fun showExpiredTokenDialog(userName: String){
        this.mExpiredTokenDialog = Dialog(this)
        mExpiredTokenDialog!!.setContentView(R.layout.dialog_login)
        baseViewModel.userName = userName
        registerClickListenerExpiredToken()
        registerViewChangeExpiredToken()
        this.mExpiredTokenDialog!!.show()
    }
    private fun hideExpiredTokenDialog() {
        baseViewModel.isLogin.removeObservers(this)
        mExpiredTokenDialog?.dismiss()
    }
    private fun registerViewChangeExpiredToken() {
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
    private fun registerClickListenerExpiredToken() {
        this.mExpiredTokenDialog!!.findViewById<AppCompatButton>(R.id.btn_re_login_accept).setOnClickListener {
            val password = this@BaseActivity.mExpiredTokenDialog!!.findViewById<AppCompatEditText>(R.id.et_re_login).text.toString()
            if(password.isEmpty())
                Toast.makeText(this,"Please write password",Toast.LENGTH_LONG).show()
            else {
                baseViewModel.password = password
                baseViewModel.login()
                this@BaseActivity.showProgressDialog("Please waiting...")
            }
        }

        this.mExpiredTokenDialog!!.findViewById<AppCompatButton>(R.id.btn_re_login_cancel).setOnClickListener {
            this.mExpiredTokenDialog!!.dismiss()
            baseViewModel.logout()
            finishAffinity()
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    fun showProgressDialog(text: String = "Please waiting...") {
        this.mProgressDialog = Dialog(this)
        mProgressDialog!!.setContentView(R.layout.dialog_progress)
        mProgressDialog!!.findViewById<TextView>(R.id.tvProgress).text = text
        mProgressDialog!!.show()
    }
    fun hideProgressDialog() {
        mProgressDialog?.dismiss()
    }

    override fun onStart() {
        super.onStart()
        Log.e("============","check")
        registerReceiver(updateAccessToken, IntentFilter(Constant.SERVICE_ACCESS_TOKEN))
    }
    override fun onPause() {
        unregisterReceiver(updateAccessToken)
        super.onPause()

    }
    private val updateAccessToken: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.e("--------", p1?.getIntExtra(Constant.SERVICE_ACCESS_TOKEN_BOOLEAN,0).toString())
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private var updateLocationDriver: BroadcastReceiver? = null
    fun onStartUpdateLocationDriver(receiver: BroadcastReceiver) {
        this.updateLocationDriver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                Log.e("--------", p1?.getIntExtra(Constant.UPDATE_LOCATION_DRIVER_STRING,0).toString())
            }
        }
        registerReceiver(updateLocationDriver, IntentFilter(Constant.UPDATE_LOCATION_DRIVER))
    }
    fun onStopUpdateLocationDriver() {
        if(updateLocationDriver != null) unregisterReceiver(updateLocationDriver)
        updateLocationDriver = null
    }
    private var isFinishMoving: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            onStopUpdateLocationDriver()
            unregisterReceiver(this)
        }
    }
    fun onRegisterFinishMoving() {
        registerReceiver(isFinishMoving, IntentFilter(Constant.FINISH_MOVING_STRING))
    }
}