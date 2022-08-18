package com.example.driver.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.driver.R
import com.example.driver.databinding.ActivityLoginBinding
import com.example.driver.presentation.BaseActivity
import com.example.driver.presentation.main.MainActivity
import com.example.driver.presentation.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogInActivity : BaseActivity() {
    @Inject
    lateinit var logInViewModel: LogInViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewModel = logInViewModel
        registerViewChangeListener()
        registerClickListener()
    }

    private fun registerClickListener() {
        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }

    private fun registerViewChangeListener() {
        logInViewModel.isLogin.observe(this){
            when(it){
                1 -> startActivity(Intent(this,MainActivity::class.java))
                0 -> Toast.makeText(this,"Please fill username or password",Toast.LENGTH_LONG).show()
                -1 -> Toast.makeText(this,"username or password invalid",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStop() {
        Log.e("-----","Stop")
        super.onStop()

    }

    override fun onDestroy() {
        Log.e("-----","Destroy")
        super.onDestroy()
    }
}