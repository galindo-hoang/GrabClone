package com.example.driver.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.driver.R
import com.example.driver.databinding.ActivityValidateRegisterBinding
import com.example.driver.presentation.BaseActivity
import com.example.driver.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ValidateRegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityValidateRegisterBinding

    @Inject
    lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_validate_register)
        binding.lifecycleOwner = this
        binding.viewModel = signUpViewModel
        registerListenerViewChange()
    }

    private fun registerListenerViewChange() {
        signUpViewModel.checkOtp.observe(this) {
            when(it.status) {
                Status.LOADING -> this.showProgressDialog()
                Status.SUCCESS -> {
                    this.hideProgressDialog()
                    startActivity(Intent(this,UpdateAccountActivity::class.java))
                }
                Status.ERROR -> {
                    this.hideProgressDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}