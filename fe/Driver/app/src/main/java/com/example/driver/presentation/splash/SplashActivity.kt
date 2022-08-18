package com.example.driver.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.driver.R
import com.example.driver.domain.usecase.SetupServiceCurrentLocationUseCase
import com.example.driver.presentation.login.LogInActivity
import com.example.driver.presentation.main.MainActivity
import com.example.driver.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var splashViewModel: SplashViewModel
    @Inject
    lateinit var setupServiceCurrentLocationUseCase: SetupServiceCurrentLocationUseCase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel.checkLogin().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    finishAffinity()
                    if (it.data == true) {
                        setupServiceCurrentLocationUseCase.start(application)
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