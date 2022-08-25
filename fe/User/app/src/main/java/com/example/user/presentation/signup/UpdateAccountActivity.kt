package com.example.user.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.user.R
import com.example.user.databinding.ActivityUpdateAccountBinding
import com.example.user.presentation.base.BaseActivity
import com.example.user.presentation.login.LogInActivity
import com.example.user.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpdateAccountActivity : BaseActivity() {
    @Inject
    lateinit var signUpViewModel: SignUpViewModel
    private lateinit var binding: ActivityUpdateAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_update_account)
        binding.lifecycleOwner = this
        binding.viewModel = signUpViewModel
        registerViewChangeListener()
        registerClickListener()
    }

    private fun registerClickListener() {
        binding.btnRegister.setOnClickListener {
            signUpViewModel.signUpSaveAccount.observe(this){
                when(it.status){
                    Status.SUCCESS -> {
                        signUpViewModel.clear()
                        this.hideProgressDialog()
                        finishAffinity()
                        startActivity(Intent(this@UpdateAccountActivity, LogInActivity::class.java))
                    }
                    Status.LOADING -> it.data?.let { it1 -> this.showProgressDialog(it1) }
                    Status.ERROR -> {
                        this.hideProgressDialog()
                        Toast.makeText(this@UpdateAccountActivity, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun registerViewChangeListener() {
        signUpViewModel.confirmPassword.observe(this) {
            if (it != null) {
                if (it.isNotEmpty() && it != binding.etConfirmNewPassword.text.toString()) {
                    signUpViewModel.isValidPassword.postValue(false)
                } else {
                    signUpViewModel.isValidPassword.postValue(true)
                }
            }
        }
    }

}