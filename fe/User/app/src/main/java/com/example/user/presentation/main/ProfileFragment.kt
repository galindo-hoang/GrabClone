package com.example.user.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.user.R
import com.example.user.databinding.FragmentProfileBinding
import com.example.user.presentation.login.LogInActivity
import com.example.user.utils.Response
import com.example.user.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment: Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mainActivity: MainActivity

    @Inject
    lateinit var profileViewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as MainActivity
        binding = FragmentProfileBinding.inflate(layoutInflater)
        registerClickListener()
        return binding.root
    }

    private fun registerClickListener() {
        binding.btnLogOut.setOnClickListener {
            profileViewModel.logout().observe(viewLifecycleOwner){
                when (it.status){
                    Status.LOADING -> mainActivity.showProgressDialog("please waiting...")
                    Status.ERROR -> {
                        mainActivity.hideProgressDialog()
                        Toast.makeText(activity,"Cant log out", Toast.LENGTH_LONG).show()
                    }
                    Status.SUCCESS -> {
                        mainActivity.hideProgressDialog()
                        mainActivity.finishAffinity()
                        mainActivity.startActivity(Intent(activity,LogInActivity::class.java))
                    }
                }
            }
        }
    }
}