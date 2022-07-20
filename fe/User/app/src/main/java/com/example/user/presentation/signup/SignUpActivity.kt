package com.example.user.presentation.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.user.R
import com.example.user.databinding.ActivitySignUpBinding
import com.example.user.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    @Inject
    lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        binding.lifecycleOwner = this
        binding.viewModel = signUpViewModel
        registerListenerViewChange()
    }

    private fun registerListenerViewChange() {
        signUpViewModel.phoneNumber.observe(this){
            if(it.isNotEmpty() && !Constant.checkPhone(it)){
                binding.tvCheckPhoneNumber.visibility = View.VISIBLE
                signUpViewModel.isValidPhoneNumber = false
            }else{
                binding.tvCheckPhoneNumber.visibility = View.GONE
                signUpViewModel.isValidPhoneNumber = true
            }
        }
        signUpViewModel.confirmPassword.observe(this){
            if(it.isNotEmpty() && it != binding.etConfirmNewPassword.text.toString()){
                binding.tvCheckConfirmNewPassword.visibility = View.VISIBLE
                signUpViewModel.isValidPassword = false
            }else {
                binding.tvCheckConfirmNewPassword.visibility = View.GONE
                signUpViewModel.isValidPassword = true
            }
        }
    }
}