package com.example.data.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datamodels.PokePosition
import com.example.data.service.firestore.FireStore
import com.example.data.service.firestore.IFireStore
import com.example.data.utils.Constants.Companion.POKE_DATE_FORMAT
import com.example.data.utils.Constants.Companion.POKE_POSITIONS_RECORDS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class LocationsViewModel(
    private val loadingComponentViewModel: LoadingComponentViewModel,
    private val pokeCloud: IFireStore
) : ViewModel() {

    private val _currentPokePositionState: MutableStateFlow<PokePosition> =
        MutableStateFlow(PokePosition(19.398432, -99.133332, ""))
    val currentPokePosition: StateFlow<PokePosition> = _currentPokePositionState.asStateFlow()

    private val _pokePositionsListState: MutableStateFlow<ArrayList<PokePosition>> =
        MutableStateFlow(arrayListOf())
    val pokePositionsList: StateFlow<ArrayList<PokePosition>> =
        _pokePositionsListState.asStateFlow()


    private var db: FirebaseFirestore = Firebase.firestore

    fun setCurrentPokePosition(latitude: Double, longitude: Double) {
        val sdf = SimpleDateFormat(POKE_DATE_FORMAT)
        val currentDate = sdf.format(Date())
        _currentPokePositionState.value = PokePosition(latitude, longitude, currentDate)
    }

    private fun setPokePositionsList(positions: ArrayList<PokePosition>) {
        _pokePositionsListState.value = positions
    }


    fun getAllPokePosition() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingComponentViewModel.showLoading(true)
            pokeCloud.getAllPokePositions(onSuccess = {pokePositions ->
                setPokePositionsList(pokePositions)
            }, onError = {
                //TOPDO: Show error message
            })
        }.invokeOnCompletion {
            loadingComponentViewModel.showLoading(false)
        }
    }
}