package com.example.user.presentation

import android.app.Dialog
import android.content.*
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.user.R
import com.example.user.databinding.DialogLoginBinding
import com.example.user.presentation.login.LogInActivity
import com.example.user.utils.Constant
import com.example.user.utils.Status
import javax.inject.Inject

open class BaseActivity @Inject constructor(): AppCompatActivity() {
    @Inject
    lateinit var baseViewModel: BaseViewModel

    private var mProgressDialog: Dialog? = null
    private var mExpiredTokenDialog: Dialog? = null

    fun showExpiredTokenDialog(userName: String){
        this.mExpiredTokenDialog = Dialog(this)
        val dialogLoginBinding = DialogLoginBinding.inflate(LayoutInflater.from(this))
        mExpiredTokenDialog?.setContentView(dialogLoginBinding.root)
        baseViewModel.userName = userName
        baseViewModel.isLogin.observe(this) {
            when(it.status){
                Status.LOADING -> this.showProgressDialog()
                Status.ERROR -> {
                    this.hideProgressDialog()
                    when(it.responseCode) {
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
                Status.SUCCESS -> {
                    baseViewModel.password = null
                    baseViewModel.userName = null
                    this.hideProgressDialog()
                    this.mExpiredTokenDialog?.dismiss()
                }
            }
        }
        dialogLoginBinding.btnReLoginAccept.setOnClickListener {
            val password = this@BaseActivity.mExpiredTokenDialog?.findViewById<AppCompatEditText>(R.id.et_re_login)?.text.toString()
            if(password.isEmpty())
                Toast.makeText(this,"Please write password",Toast.LENGTH_LONG).show()
            else {
                baseViewModel.password = password
                baseViewModel.login(BaseApplication.token)
                this@BaseActivity.showProgressDialog("Please waiting...")
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

    /////////////////////////////////////////////////////////////////////////////////////
    fun showProgressDialog(text: String = "Please waiting...") {
        this.mProgressDialog = Dialog(this)
        mProgressDialog!!.setContentView(R.layout.dialog_progress)
        mProgressDialog!!.findViewById<TextView>(R.id.tvProgress).text = text
        mProgressDialog!!.show()
    }
    fun hideProgressDialog() { mProgressDialog?.dismiss() }
    ////////////////////////////////////////////////////////////////////////////
    private var updateLocationDriver: BroadcastReceiver? = null
    fun registerLocationDriver(receiver: BroadcastReceiver) {
        this.updateLocationDriver = receiver
        registerReceiver(updateLocationDriver, IntentFilter(Constant.UPDATE_LOCATION_DRIVER))
    }
    fun unRegisterLocationDriver() {
        if(updateLocationDriver != null) unregisterReceiver(updateLocationDriver)
        updateLocationDriver = null
    }
    private var isFinishMoving: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            unRegisterLocationDriver()
            unregisterReceiver(this)
        }
    }
    fun registerFinishMoving() {
        registerReceiver(isFinishMoving, IntentFilter(Constant.FINISH_MOVING_STRING))
    }
}