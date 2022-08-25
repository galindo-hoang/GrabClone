package com.example.user.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.user.R
import com.example.user.presentation.base.BaseApplication
import com.example.user.presentation.login.LogInActivity
import com.example.user.presentation.main.MainActivity
import com.example.user.utils.Status
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var splashViewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                BaseApplication.token = task.result
                Log.e("--------",BaseApplication.token)
                splashViewModel.checkLogin().observe(this) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            finishAffinity()
                            if (it.data == true) {
                                startActivity(
                                    Intent(this@SplashActivity, MainActivity::class.java)
                                )

                            }
                            else if (it.data == false)
                                startActivity(Intent(this@SplashActivity, LogInActivity::class.java))
                        }
                        else -> {}
                    }
                }
            }else Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
        }
    }
}