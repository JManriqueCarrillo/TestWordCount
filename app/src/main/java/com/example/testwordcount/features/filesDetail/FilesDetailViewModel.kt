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
): ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Empty)
    val viewState: StateFlow<ViewState> get() = _viewState

    private val _fileProcessed = MutableLiveData<TextFile>()
    val fileProcessed: LiveData<TextFile> get() = _fileProcessed

    lateinit var textFileProcessed: TextFile

    init{
        _viewState.value = ViewState.Empty
    }

    fun readFile(name: String){
        //Show progress
        viewModelScope.launch {
            val fileReaded = filesRepository.readFile(name)
            val fileProcessed = filesRepository.processText(name, fileReaded)

            Log.d("RES", fileProcessed.toString())

            //Hide loader
        }
    }

    fun getTextFile(): TextFile{
        return textFileProcessed
    }

    fun getWordTimes(): Map<String, Int>{
        return textFileProcessed.mapTimes
    }

    fun getWordPosition(): List<String>{
        return textFileProcessed.mapPosition.toList()
    }

    fun getWordAlphabetical(): List<String>{
        return textFileProcessed.mapOrder.toList()
    }


}