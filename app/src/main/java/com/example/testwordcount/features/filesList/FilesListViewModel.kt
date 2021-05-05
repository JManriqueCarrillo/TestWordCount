package com.example.testwordcount.features.filesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testwordcount.repository.AssetsRepository
import com.example.testwordcount.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilesListViewModel @Inject constructor(
    private val assetsRepository: AssetsRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Empty)
    val viewState: StateFlow<ViewState> get() = _viewState

    private val _filesList = MutableLiveData<List<String>>()
    val filesList: LiveData<List<String>> get() = _filesList

    init {
        _viewState.value = ViewState.Empty
    }

    fun getFiles(){
        _viewState.value = ViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val files = assetsRepository.getFiles()
            _filesList.postValue(files)
            _viewState.value = ViewState.Success
        }

    }

}