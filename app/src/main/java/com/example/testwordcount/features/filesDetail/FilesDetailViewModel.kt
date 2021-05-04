package com.example.testwordcount.features.filesDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testwordcount.entities.TextFile
import com.example.testwordcount.repository.FilesRepository
import com.example.testwordcount.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilesDetailViewModel @Inject constructor(
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

    init {
        _viewState.value = ViewState.Empty
    }

    fun readFile(name: String) {
        _viewState.value = ViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val fileReaded = filesRepository.readFile(name)
            val fileProcessed = filesRepository.processText(name, fileReaded)

            Log.d("RES", fileProcessed.toString())

            _textFileProcessed = fileProcessed
            _originalItemsList = fileProcessed.mapPosition
            _listData.postValue(fileProcessed.mapPosition)
            _listType.postValue("Word position")
            _viewState.value = ViewState.Success
        }
    }

    fun filter(query: String) {
        _filteredItemsList = _originalItemsList.filter {
            it.contains(query)
            //it.toLowerCase().contains(query.toLowerCase())
        }
        _listData.postValue(_filteredItemsList as MutableList<String>?)
    }

    fun getTextFile(): TextFile {
        return _textFileProcessed
    }

    fun getWordTimes(): List<String> {
        _originalItemsList = _textFileProcessed.mapTimes
        _listType.postValue("Word times")
        return _textFileProcessed.mapTimes
    }

    fun getWordPosition(): List<String> {
        _originalItemsList = _textFileProcessed.mapPosition
        _listType.postValue("Word position")
        return _textFileProcessed.mapPosition
    }

    fun getWordAlphabetical(): List<String> {
        _originalItemsList = _textFileProcessed.mapOrder
        _listType.postValue("Word alphabetical")
        return _textFileProcessed.mapOrder
    }


}