package com.example.data.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationsViewModel : ViewModel() {

    private val _latitudeState: MutableStateFlow<Double> =  MutableStateFlow( 0.0 )
    val latitude: StateFlow<Double> = _latitudeState.asStateFlow()

    private val _longitudeState: MutableStateFlow<Double> =  MutableStateFlow( 0.0 )
    val longitude: StateFlow<Double> = _longitudeState.asStateFlow()

    fun setLocation(latitude:Double, longitude:Double){
        _latitudeState.value = latitude
        _longitudeState.value = longitude
    }
}