package com.example.driver.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.driver.databinding.FragmentHomeBinding
import com.example.driver.domain.usecase.SetupServiceCurrentLocationUseCase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainActivity: MainActivity
    @Inject
    lateinit var setupServiceCurrentLocationUseCase: SetupServiceCurrentLocationUseCase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        this.mainActivity = activity as MainActivity
//        setRecycleView()
//        registerClickListener()
//        registerViewChangeListener()
        Log.e("-------","hello")
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            Log.e("====",it.result)
        }
        return binding.root
    }


    private fun registerViewChangeListener() {}

    private fun registerClickListener() {
        binding.btnStartingListening.setOnClickListener {
            setupServiceCurrentLocationUseCase.start(requireActivity().application)
            this.mainActivity.startLooking()
        }
        binding.btnStopListening.setOnClickListener {
            this.mainActivity.stopLooking()
            setupServiceCurrentLocationUseCase.stop()
        }
    }

    private fun setRecycleView() {}
}