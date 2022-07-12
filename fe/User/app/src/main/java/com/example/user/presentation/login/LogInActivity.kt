package com.example.user.presentation.login

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.user.R
import com.example.user.databinding.ActivityLoginBinding
import com.example.user.presentation.BaseActivity
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
    }
}