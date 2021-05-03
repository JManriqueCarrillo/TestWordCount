package com.example.testwordcount.utils

sealed class ViewState {
    object Success : ViewState()
    object Failure : ViewState()
    object Loading : ViewState()
    object Empty : ViewState()
}