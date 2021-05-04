package com.example.testwordcount.features.filesDetail

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testwordcount.R
import com.example.testwordcount.adapters.FileDetailAdapter
import com.example.testwordcount.databinding.ActivityFilesDetailBinding
import com.example.testwordcount.features.filesDetail.popup.ChooseTypePopup
import com.example.testwordcount.features.filesDetail.popup.ChooseTypeViewModel
import com.example.testwordcount.utils.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FilesDetailActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityFilesDetailBinding
    private lateinit var viewModel: FilesDetailViewModel
    private lateinit var typeViewModel: ChooseTypeViewModel

    private lateinit var fileAdapter: FileDetailAdapter
    private lateinit var searchView: SearchView

    private lateinit var list_type: ChooseTypeViewModel.LIST_TYPE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLifecycleScope()
        setListeners()
        setAdapter()

        val fileName = intent.getStringExtra("file_name")

        if (fileName != null && fileName.isNotBlank()) {
            viewModel.readFile(fileName)
            binding.fileName.text = fileName
        }
    }

    private fun setListeners() {
        viewModel = ViewModelProvider(this).get()
        viewModel.listData.observe(this, Observer {
            fileAdapter.setData(it)
            fileAdapter.notifyDataSetChanged()
            list_type = ChooseTypeViewModel.LIST_TYPE.POSITION
        })
        viewModel.listType.observe(this, Observer {
            binding.sortTxt.text = it
        })

        typeViewModel = ViewModelProvider(this).get()
        typeViewModel.list_type.observe(this, Observer {
            when (it) {
                ChooseTypeViewModel.LIST_TYPE.POSITION -> {
                    if (list_type != ChooseTypeViewModel.LIST_TYPE.POSITION) {
                        fileAdapter.setData(viewModel.getWordPosition())
                    }
                }
                ChooseTypeViewModel.LIST_TYPE.ALPHABETICAL -> {
                    if (list_type != ChooseTypeViewModel.LIST_TYPE.ALPHABETICAL) {
                        fileAdapter.setData(viewModel.getWordAlphabetical())
                    }
                }
                ChooseTypeViewModel.LIST_TYPE.TIMES -> {
                    if (list_type != ChooseTypeViewModel.LIST_TYPE.TIMES) {
                        fileAdapter.setData(viewModel.getWordTimes())
                    }
                }
            }
            fileAdapter.notifyDataSetChanged()
            list_type = it
        })

    }

    private fun setAdapter() {
        fileAdapter = FileDetailAdapter(this, mutableListOf()) { text ->
            //We can do anything with the text, like search on Wikipedia for example
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                ChooseTypePopup().show(supportFragmentManager, ChooseTypePopup.TAG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchView.clearFocus()
        hideKeyboard(currentFocus ?: View(this))
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        viewModel.filter(query)
        return true
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


}