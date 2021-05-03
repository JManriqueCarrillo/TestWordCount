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

    private val _fileProcessed = MutableLiveData<TextFile>()
    val fileProcessed: LiveData<TextFile> get() = _fileProcessed

    private lateinit var _textFileProcessed: TextFile

    init {
        _viewState.value = ViewState.Empty
    }

    fun readFile(name: String) {
        _viewState.value = ViewState.Loading
        viewModelScope.launch {
            val fileReaded = filesRepository.readFile(name)
            val fileProcessed = filesRepository.processText(name, fileReaded)

            Log.d("RES", fileProcessed.toString())

            _textFileProcessed = fileProcessed
            _fileProcessed.postValue(fileProcessed)
            _viewState.value = ViewState.Success
        }
    }

    fun getTextFile(): TextFile {
        return _textFileProcessed
    }

    fun getWordTimes(): List<String> {
        return _textFileProcessed.mapTimes
    }

    fun getWordPosition(): List<String> {
        return _textFileProcessed.mapPosition
    }

    fun getWordAlphabetical(): List<String> {
        return _textFileProcessed.mapOrder
    }


}