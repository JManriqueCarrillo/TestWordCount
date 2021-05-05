package com.example.testwordcount.features.filesDetail.popup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChooseTypeViewModel: ViewModel() {

    enum class LIST_TYPE{
        POSITION,
        ALPHABETICAL,
        TIMES
    }
    
    private val _listType = MutableLiveData<LIST_TYPE>()
    val listType: LiveData<LIST_TYPE> get() = _listType

    fun sentListType(type: LIST_TYPE) {
        _listType.value = type
    }
}