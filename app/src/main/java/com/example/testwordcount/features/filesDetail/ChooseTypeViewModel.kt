package com.example.testwordcount.features.filesDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChooseTypeViewModel: ViewModel() {

    enum class LIST_TYPE{
        POSITION,
        ALPHABETICAL,
        TIMES
    }
    
    private val _list_type = MutableLiveData<LIST_TYPE>()
    val list_type: LiveData<LIST_TYPE> get() = _list_type

    fun sentListType(type: LIST_TYPE) {
        _list_type.value = type
    }
}