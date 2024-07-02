package com.example.data.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoadingComponentViewModel : ViewModel() {

    private val _showLoadingState: MutableStateFlow<Boolean> =  MutableStateFlow( false )
    val showLoading: StateFlow<Boolean> = _showLoadingState.asStateFlow()

    fun showLoading(flag:Boolean){
        _showLoadingState.value = flag
    }
}