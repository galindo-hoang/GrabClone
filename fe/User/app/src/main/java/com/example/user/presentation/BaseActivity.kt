package com.example.user.presentation

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.user.R
import com.example.user.presentation.login.LogInActivity
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
}