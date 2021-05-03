package com.example.testwordcount.features.filesDetail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testwordcount.adapters.FileDetailAdapter
import com.example.testwordcount.databinding.ActivityFilesDetailBinding
import com.example.testwordcount.utils.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FilesDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityFilesDetailBinding
    private lateinit var viewModel: FilesDetailViewModel

    private lateinit var fileAdapter: FileDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
        setAdapter()
        setupLifecycleScope()

        viewModel.readFile("plrabn12.txt")
    }

    private fun setListeners(){
        viewModel = ViewModelProvider(this).get()
        viewModel.fileProcessed.observe(this, Observer {
            fileAdapter.setData(it.mapTimes)
            fileAdapter.notifyDataSetChanged()
        })
    }

    private fun setAdapter(){
        fileAdapter = FileDetailAdapter(this, mutableListOf()){ text ->
            //We can do anything with the text, like search on Wikipedia for example
        }
        binding.itemsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.itemsList.adapter = fileAdapter
    }

    private fun setupLifecycleScope() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { state ->
                when (state) {
                    ViewState.Failure -> {
                        binding.progressBar.isVisible = false
                    }
                    ViewState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    ViewState.Success -> {
                        binding.progressBar.isVisible = false
                    }
                    ViewState.Empty -> {
                        binding.progressBar.isVisible = false
                    }
                }
            }
        }
    }



}