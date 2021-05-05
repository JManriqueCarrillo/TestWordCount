package com.example.testwordcount.features.filesDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testwordcount.R
import com.example.testwordcount.adapters.infiniteScroll.VIEW_TYPE.NUMBER_ITEMS_PER_PAGE
import com.example.testwordcount.entities.TextFile
import com.example.testwordcount.features.filesDetail.popup.ChooseTypeViewModel
import com.example.testwordcount.repository.FilesRepository
import com.example.testwordcount.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class FilesDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val filesRepository: FilesRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Empty)
    val viewState: StateFlow<ViewState> get() = _viewState

    private val _listData = MutableLiveData<List<String>>()
    val listData: LiveData<List<String>> get() = _listData

    private val _listType = MutableLiveData<String>()
    val listType: LiveData<String> get() = _listType

    private lateinit var _textFileProcessed: TextFile
    private lateinit var _originalItemsList: List<String>
    private lateinit var _filteredItemsList: List<String>

    fun readFile(name: String) {
        _viewState.value = ViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val fileRead = filesRepository.readFile(name)
            val fileProcessed = filesRepository.processText(name, fileRead)

            _textFileProcessed = fileProcessed
            _originalItemsList = fileProcessed.mapPosition
            _filteredItemsList = _originalItemsList

            _listData.postValue(fileProcessed.mapPosition)
            _listType.postValue(context.getString(R.string.word_position))

            _viewState.value = ViewState.Success
        }
    }

    fun filter(query: String) {
        _filteredItemsList = _originalItemsList.filter {
            it.contains(query)
            //it.toLowerCase().contains(query.toLowerCase()) //To ignore Case Sensitive
        }
        _listData.postValue(_filteredItemsList as MutableList<String>?)
    }

    fun getMoreData(pageScroll: Int): List<String?> {
        val start = min(pageScroll * NUMBER_ITEMS_PER_PAGE, _filteredItemsList.size)
        val end = start + NUMBER_ITEMS_PER_PAGE
        return _filteredItemsList.subList(start, min(end, _filteredItemsList.size))
    }

    fun getTextFile(): TextFile {
        return _textFileProcessed
    }

    fun getDataByType(type: ChooseTypeViewModel.LIST_TYPE): List<String>{
        return when (type) {
            ChooseTypeViewModel.LIST_TYPE.POSITION -> {
                getWordPosition()
            }
            ChooseTypeViewModel.LIST_TYPE.ALPHABETICAL -> {
                getWordAlphabetical()
            }
            ChooseTypeViewModel.LIST_TYPE.TIMES -> {
                getWordTimes()
            }
        }
    }

    private fun getWordTimes(): List<String> {
        _originalItemsList = _textFileProcessed.mapTimes
        _filteredItemsList = _originalItemsList
        _listType.postValue(context.getString(R.string.word_times))
        return _textFileProcessed.mapTimes.subList(
            0,
            min(NUMBER_ITEMS_PER_PAGE, _textFileProcessed.mapTimes.size)
        )
    }

    private fun getWordPosition(): List<String> {
        _originalItemsList = _textFileProcessed.mapPosition
        _filteredItemsList = _originalItemsList
        _listType.postValue(context.getString(R.string.word_position))
        return _textFileProcessed.mapPosition.subList(
            0,
            min(NUMBER_ITEMS_PER_PAGE, _textFileProcessed.mapPosition.size)
        )
    }

    private fun getWordAlphabetical(): List<String> {
        _originalItemsList = _textFileProcessed.mapOrder
        _filteredItemsList = _originalItemsList
        _listType.postValue(context.getString(R.string.word_alphabetical))
        return _textFileProcessed.mapOrder.subList(
            0,
            min(NUMBER_ITEMS_PER_PAGE, _textFileProcessed.mapOrder.size)
        )
    }

}