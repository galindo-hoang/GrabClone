package com.example.user.presentation

import android.content.*
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.user.R
import com.example.user.presentation.login.LogInActivity
import com.example.user.utils.Constant
import javax.inject.Inject

open class BaseActivity @Inject constructor(): AppCompatActivity(){
    fun showExpiredTokenDialog(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog
            .setIcon(R.drawable.ic_launcher_foreground)
            .setTitle("WARNING")
            .setMessage("your account is expired. Do you want to continue Login ?")
            .setPositiveButton("Yes"
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()

            }.setNegativeButton("No"){ dialogInterface, _ ->
                dialogInterface.dismiss()
                finishAffinity()
                startActivity(Intent(this, LogInActivity::class.java))
            }
    }

    fun registerBroadcastReceiver(){
        registerReceiver(updateAccessToken, IntentFilter(Constant.SERVICE_ACCESS_TOKEN))
    }

    private val updateAccessToken: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.e("--------", p1?.getIntExtra(Constant.SERVICE_ACCESS_TOKEN_BOOLEAN,0).toString())
        }

    }
}