package com.example.data.service.firestore

import android.content.ContentValues
import android.util.Log
import com.example.data.datamodels.PokePosition
import com.example.data.utils.Constants
import com.example.data.utils.Constants.Companion.POKE_LATITUDE
import com.example.data.utils.Constants.Companion.POKE_LONGITUDE
import com.example.data.utils.Constants.Companion.POKE_PUBLISH_DATE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.core.component.KoinComponent

class FireStore : IFireStore, KoinComponent {

    private var db : FirebaseFirestore = Firebase.firestore

    override fun addNewPokePosition(
        pokePosition: PokePosition,
        onSuccess: (PokePosition) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        db.collection(Constants.POKE_POSITIONS_RECORDS)
            .add(pokePosition)
            .addOnSuccessListener {
                onSuccess(pokePosition)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }

    override fun getAllPokePositions(
        onSuccess: (ArrayList<PokePosition>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        db.collection(Constants.POKE_POSITIONS_RECORDS)
            .get()
            .addOnSuccessListener { result ->
                val pokePositionsList : ArrayList<PokePosition> = arrayListOf()
                result?.forEach {
                    val pokePosition = PokePosition(it.data[POKE_LATITUDE].toString().toDouble(),
                        it.data[POKE_LONGITUDE].toString().toDouble(),
                        it.data[POKE_PUBLISH_DATE].toString())
                    pokePositionsList.add(pokePosition)
                }
                onSuccess(pokePositionsList)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

}