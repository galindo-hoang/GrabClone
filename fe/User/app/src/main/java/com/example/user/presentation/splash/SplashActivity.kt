package com.example.user.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.user.R
import com.example.user.presentation.login.LogInActivity
import com.example.user.presentation.main.MainActivity
import com.example.user.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var splashViewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
    }
}