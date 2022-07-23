package com.example.user.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.user.R
import com.example.user.databinding.ActivityLoginBinding
import com.example.user.presentation.BaseActivity
import com.example.user.presentation.signup.SignUpActivity
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
                1 -> Toast.makeText(this,"Login success",Toast.LENGTH_LONG).show()
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