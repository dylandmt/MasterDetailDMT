package com.example.data.service.firestore

import com.example.data.datamodels.PokePosition

interface IFireStore {
    fun addNewPokePosition(pokePosition: PokePosition, onSuccess: (PokePosition) -> Unit, onError: (Throwable) -> Unit)
    fun getAllPokePositions(onSuccess: (pokePositions: ArrayList<PokePosition>) -> Unit, onError: (Throwable) -> Unit)
}