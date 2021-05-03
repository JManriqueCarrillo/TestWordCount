package com.example.testwordcount.features.filesDetail

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.testwordcount.databinding.ActivityFilesDetailBinding

class FilesDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityFilesDetailBinding
    private lateinit var viewModel: FilesDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityFilesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

    }

}