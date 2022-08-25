package com.example.driver.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.driver.R
import com.example.driver.databinding.ActivitySignUpBinding
import com.example.driver.presentation.BaseActivity
import com.example.driver.presentation.login.LogInActivity
import com.example.driver.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    @Inject
    lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        binding.lifecycleOwner = this
        binding.viewModel = signUpViewModel
        registerViewChangeListener()
        registerClickListener()
    }

    private fun registerClickListener() {
        binding.btnHaveAccount.setOnClickListener {
            startActivity(Intent(this,LogInActivity::class.java))
        }
    }

    private fun registerViewChangeListener() {
        signUpViewModel.phoneNumber.observe(this){
            if(it != null) {
                signUpViewModel.isValidPhoneNumber.value =
                    !(it.isNotEmpty() && !Constant.checkPhone(it))
            }
        }

        signUpViewModel.otp.observe(this){

            Log.e("otp",it.toString())
            if(it == 0) this.showProgressDialog()
            else if(it != -1) {
                this.hideProgressDialog()
                startActivity(Intent(this, ValidateRegisterActivity::class.java))
            }
            else {
                this.hideProgressDialog()
                Toast.makeText(this, "cant register account", Toast.LENGTH_LONG).show()
            }
        }
    }
}