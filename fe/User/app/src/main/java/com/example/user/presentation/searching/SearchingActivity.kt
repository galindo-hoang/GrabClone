package com.example.user.presentation.searching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.user.R
import com.example.user.databinding.ActivitySearchingBinding
import com.example.user.presentation.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchingActivity : BaseActivity() {
    @Inject
    lateinit var searchingViewModel: SearchingViewModel

    private lateinit var binding: ActivitySearchingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_searching)
        binding.lifecycleOwner = this
        binding.viewModel = searchingViewModel
    }
}