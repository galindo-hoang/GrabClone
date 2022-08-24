package com.example.driver.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.driver.databinding.FragmentHomeBinding
import com.example.driver.presentation.BaseApplication
import com.example.driver.utils.Response
import com.example.driver.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainActivity: MainActivity
    @Inject
    lateinit var homeFragmentViewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("-----------",BaseApplication.token)
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        this.mainActivity = activity as MainActivity
        registerClickListener()
        registerViewChangeListener()
        return binding.root
    }


    private fun registerViewChangeListener() {}

    private fun registerClickListener() {
        binding.btnStartListening.setOnClickListener {
            homeFragmentViewModel.startListening().observe(viewLifecycleOwner){
                common(it) { this.mainActivity.startLooking() }
            }
        }
        binding.btnStopListening.setOnClickListener {
            homeFragmentViewModel.stopListening().observe(viewLifecycleOwner) {
                common(it) { this.mainActivity.stopLooking() }
            }
        }
    }

    private fun common(response: Response<out Any>, func:() -> Unit) {
        when(response.status){
            Status.LOADING -> this.mainActivity.showProgressDialog()
            Status.ERROR -> {
                this.mainActivity.hideProgressDialog()
                if(response.codeResponse == -2) this.mainActivity.showExpiredTokenDialog(response.message.toString())
                else Toast.makeText(activity,response.message.toString(),Toast.LENGTH_LONG).show()
            }
            Status.SUCCESS -> {
                func()
                this.mainActivity.hideProgressDialog()
            }
        }
    }
}