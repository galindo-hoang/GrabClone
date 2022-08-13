package com.example.user.presentation.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.user.R
import com.example.user.databinding.ActivityValidateRegisterBinding
import com.example.user.presentation.login.LogInActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ValidateRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityValidateRegisterBinding
    private var otp: Int = 0
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
        signUpViewModel.checkOtp.observe(this){
            if(it){
                if(signUpViewModel.validateOTP() == 1){
                    Toast.makeText(this, "validate success",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,UpdateAccountActivity::class.java))
                }else{
                    Log.e("-------","fail")
                }
            }else Toast.makeText(this,"OTP invalid",Toast.LENGTH_LONG).show()
        }
    }
}