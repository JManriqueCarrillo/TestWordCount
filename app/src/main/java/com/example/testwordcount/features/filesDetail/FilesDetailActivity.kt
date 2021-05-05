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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testwordcount.R
import com.example.testwordcount.adapters.FileDetailAdapter
import com.example.testwordcount.adapters.infiniteScroll.InfiniteScroll
import com.example.testwordcount.adapters.infiniteScroll.OnLoadMoreListener
import com.example.testwordcount.adapters.infiniteScroll.VIEW_TYPE.NUMBER_ITEMS_PER_PAGE
import com.example.testwordcount.databinding.ActivityFilesDetailBinding
import com.example.testwordcount.features.filesDetail.popup.ChooseTypePopup
import com.example.testwordcount.features.filesDetail.popup.ChooseTypeViewModel
import com.example.testwordcount.utils.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

@AndroidEntryPoint
class FilesDetailActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    companion object{
        const val PARAM_FILE_NAME = "file_name"
    }

    private lateinit var binding: ActivityFilesDetailBinding
    private lateinit var viewModel: FilesDetailViewModel
    private lateinit var typeViewModel: ChooseTypeViewModel

    private lateinit var fileAdapter: FileDetailAdapter
    private lateinit var searchView: SearchView

    private lateinit var loadMoreItemsCells: List<String?>
    private lateinit var scrollListener: InfiniteScroll

    private var pageScroll = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLifecycleScope()
        setListeners()
        setAdapter()

        val fileName = intent.getStringExtra(PARAM_FILE_NAME)

        if (fileName != null && fileName.isNotBlank()) {
            viewModel.readFile(fileName)
            binding.fileName.text = fileName
        }
    }

    private fun setListeners() {
        viewModel = ViewModelProvider(this).get()
        viewModel.listData.observe(this, {
            fileAdapter.setData(it.subList(0, min(NUMBER_ITEMS_PER_PAGE, it.size)))
            fileAdapter.notifyDataSetChanged()
            pageScroll = 1
        })
        viewModel.listType.observe(this, {
            binding.sortTxt.text = it
        })
        viewModel.numberItems.observe(this, {
            binding.numberTxt.text = "($it)"
        })

        typeViewModel = ViewModelProvider(this).get()
        typeViewModel.listType.observe(this, {
            fileAdapter.setData(viewModel.getDataByType(it))
            pageScroll = 0
            fileAdapter.notifyDataSetChanged()
            binding.itemsList.scrollToPosition(0)
        })

    }

    private fun setAdapter() {
        fileAdapter = FileDetailAdapter(this, mutableListOf()) { text ->
            //We can do anything with the text, like search on Wikipedia for example
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.itemsList.layoutManager = layoutManager
        binding.itemsList.setHasFixedSize(true)
        scrollListener = InfiniteScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreData()
            }
        })
        binding.itemsList.adapter = fileAdapter
        binding.itemsList.addOnScrollListener(scrollListener)
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
                if (!binding.progressBar.isVisible)
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
        return if (binding.progressBar.isVisible) {
            false
        } else {
            pageScroll = 0
            viewModel.filter(query)
            true
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun loadMoreData() {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { fileAdapter.addLoadingView() }
            pageScroll++
            loadMoreItemsCells = viewModel.getMoreData(pageScroll)
            //delay(500) //To watch the spinner
            withContext(Dispatchers.Main) { fileAdapter.removeLoadingView() }
            fileAdapter.addData(loadMoreItemsCells)
            scrollListener.setLoaded()
            withContext(Dispatchers.Main) { fileAdapter.notifyDataSetChanged() }
        }
    }
}