package com.example.testwordcount.features.filesDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.example.testwordcount.databinding.ActivityFilesDetailBinding
import com.example.testwordcount.utils.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FilesDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityFilesDetailBinding
    private lateinit var viewModel: FilesDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()
        viewModel.readFile("plrabn12.txt")
    }

    private fun setupLifecycleScope() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { state ->
                when (state) {
                    ViewState.Failure -> {
                    }
                    ViewState.Loading -> {
                    }
                    ViewState.Success -> {
                    }
                    ViewState.Empty -> {
                    }
                }
            }
        }
    }



}