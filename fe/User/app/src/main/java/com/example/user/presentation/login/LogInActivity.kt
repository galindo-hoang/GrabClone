package com.example.user.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.user.R
import com.example.user.databinding.ActivityLoginBinding
import com.example.user.presentation.BaseActivity
import com.example.user.presentation.main.MainActivity
import com.example.user.presentation.signup.SignUpActivity
import com.example.user.utils.Status
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
            when(it.status){
                Status.LOADING -> this.showProgressDialog()
                Status.SUCCESS -> {
                    this.hideProgressDialog()
                    startActivity(Intent(this,MainActivity::class.java))
                }
                Status.ERROR -> {
                    this.hideProgressDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}