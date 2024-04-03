package com.example.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonData
import com.example.data.repository.PokemonRepository
import com.example.data.usecase.GetNextPokemonListUseCase
import com.example.data.usecase.GetPokemonListUseCase
import com.example.data.usecase.GetPokemonSpritesUseCase
import com.example.data.utils.Constants.Companion.BASE_MAIN_URL
import com.example.data.utils.Constants.Companion.EMPTY_STRING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonSpritesUseCase: GetPokemonSpritesUseCase,
    private val getNextPokemonListUseCase: GetNextPokemonListUseCase
) : ViewModel() {
    private val _pokemonListState: MutableStateFlow<ArrayList<Pokemon>> = MutableStateFlow(
        arrayListOf()
    )
    val pokemonList: StateFlow<ArrayList<Pokemon>> = _pokemonListState.asStateFlow()

    private val _pokemonListDisplayState: MutableStateFlow<ArrayList<PokemonData>> =
        MutableStateFlow(
            arrayListOf()
        )
    val pokemonListDisplay: StateFlow<ArrayList<PokemonData>> =
        _pokemonListDisplayState.asStateFlow()

    private val _nextPokemonListUrlState: MutableStateFlow<String> = MutableStateFlow(EMPTY_STRING)
    private val nextPokemonListUrl: StateFlow<String> = _nextPokemonListUrlState.asStateFlow()

    private val _pokemonDataSelectedState: MutableStateFlow<PokemonData?> = MutableStateFlow(null)
    val pokemonDataSelected: StateFlow<PokemonData?> = _pokemonDataSelectedState.asStateFlow()

    fun setPokemonDataSelected(data : PokemonData){
        _pokemonDataSelectedState.value = data
    }
    private fun setPokemonList(pokemonList: ArrayList<Pokemon>) {
        _pokemonListState.value = pokemonList
    }

    private fun setPokemonDisplayList(pokemonList: ArrayList<PokemonData>) {
        _pokemonListDisplayState.value = pokemonList
    }

    private fun setNextPokemonListUrl(url: String) {
        _nextPokemonListUrlState.value = url
    }

    fun getPokemonList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getPokemonListUseCase.invoke()
            if (response?.results?.isNotEmpty() == true) {
                setNextPokemonListUrl(response.next)
                setPokemonList(response.results)
            }
        }.invokeOnCompletion {
            if (pokemonList.value.isNotEmpty()) {
                getSprites()
            }
        }
    }

    fun getNextPokemonItems() {
        if (nextPokemonListUrl.value != EMPTY_STRING) {
            val url = nextPokemonListUrl.value.replace(BASE_MAIN_URL + "pokemon", EMPTY_STRING)
            val limit = url.substringAfter("limit=").filter { it.isDigit() }.toInt()
            val offset =
                url.substringAfter("offset=").replace("&limit=$limit", "").filter { it.isDigit() }
                    .toInt()

            viewModelScope.launch(Dispatchers.IO) {
                val response = getNextPokemonListUseCase.invoke(offset, limit)
                if (response?.results?.isNotEmpty() == true) {
                    setNextPokemonListUrl(response.next)
                    val currentPokemonList = pokemonList.value
                    response.results.forEach {
                        currentPokemonList.add(it)
                    }
                    setPokemonList(currentPokemonList)
                }
            }.invokeOnCompletion {
                if (pokemonList.value.isNotEmpty()) {
                    //
                }
            }
        }
    }

    private fun getSprites() {
        val tempPokemonDisplayList: ArrayList<PokemonData> = arrayListOf()
        viewModelScope.launch(Dispatchers.IO) {
            pokemonList.value.forEach {
                val pokemonDetails = getPokemonSpritesUseCase.invoke(
                    it.url.replace(
                        BASE_MAIN_URL + "pokemon/",
                        EMPTY_STRING
                    )
                )
                pokemonDetails?.sprites?.let { sprite ->
                    tempPokemonDisplayList.add(
                        PokemonData(
                            name = it.name,
                            urlImage = sprite.frontDefault,
                            details = pokemonDetails
                        )
                    )
                }
            }
        }.invokeOnCompletion {
            setPokemonDisplayList(tempPokemonDisplayList)
        }

    }
}