package com.example.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.datamodels.Sprite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailsViewModel : ViewModel() {
    private val _spritesListState: MutableStateFlow<ArrayList<String>> = MutableStateFlow(
        arrayListOf()
    )
    val spritesList: StateFlow<ArrayList<String>> = _spritesListState.asStateFlow()

    private val _showSpritesState: MutableStateFlow<Boolean> = MutableStateFlow(
        false
    )
    val showSprites: StateFlow<Boolean> = _showSpritesState.asStateFlow()
    fun setSpritesList(pokemonSprites: Sprite) {
        val sprites: ArrayList<String> = arrayListOf(
            pokemonSprites.backDefault,
            pokemonSprites.frontDefault,
            pokemonSprites.frontFemale,
            pokemonSprites.backFemale,
            pokemonSprites.backShiny,
            pokemonSprites.backShinyFemale,
            pokemonSprites.frontShiny,
            pokemonSprites.frontShinyFemale
        )
        val spritesFiltered = sprites.filter { !it.isNullOrEmpty() } as ArrayList
        _spritesListState.value = spritesFiltered
    }

    fun setShowSprites(flag: Boolean) {
        _showSpritesState.value = flag
    }
}