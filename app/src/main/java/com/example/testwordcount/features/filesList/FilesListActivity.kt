package com.example.testwordcount.features.filesList

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testwordcount.adapters.FileDetailAdapter
import com.example.testwordcount.databinding.ActivityFilesListBinding
import com.example.testwordcount.features.filesDetail.FilesDetailActivity
import com.example.testwordcount.utils.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FilesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilesListBinding
    private lateinit var viewModel: FilesListViewModel

    private lateinit var fileAdapter: FileDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()
        viewModel.filesList.observe(this, {
            fileAdapter.setData(it)
            fileAdapter.notifyDataSetChanged()
        })

        setupLifecycleScope()
        setAdapter()
        viewModel.getFiles()
    }

    private fun setAdapter() {
        fileAdapter = FileDetailAdapter(this, mutableListOf()) {
            intent = Intent(this, FilesDetailActivity::class.java)
            intent.putExtra(FilesDetailActivity.PARAM_FILE_NAME, it as String)
            startActivity(intent)
        }
        binding.itemsList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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