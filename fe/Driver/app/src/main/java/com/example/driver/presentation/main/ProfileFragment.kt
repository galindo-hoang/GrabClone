package com.example.driver.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.driver.databinding.FragmentProfileBinding
import com.example.driver.domain.usecase.SetupServiceCurrentLocationUseCase
import com.example.driver.presentation.login.LogInActivity
import com.example.driver.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment: Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mainActivity: MainActivity

    @Inject
    lateinit var profileFragmentViewModel: ProfileFragmentViewModel
    @Inject
    lateinit var setupServiceCurrentLocationUseCase: SetupServiceCurrentLocationUseCase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as MainActivity
        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        registerClickListener()
        return binding.root
    }
    private fun registerClickListener() {
        binding.btnLogOut.setOnClickListener {
            profileFragmentViewModel.logout().observe(viewLifecycleOwner){
                when (it.status){
                    Status.LOADING -> mainActivity.showProgressDialog()
                    Status.ERROR -> {
                        mainActivity.hideProgressDialog()
                        Toast.makeText(activity,"Cant log out", Toast.LENGTH_LONG).show()
                    }
                    Status.SUCCESS -> {
                        mainActivity.hideProgressDialog()
                        setupServiceCurrentLocationUseCase.stop()
                        mainActivity.finishAffinity()
                        mainActivity.startActivity(Intent(activity,LogInActivity::class.java))
                    }
                }
            }
        }
    }
}