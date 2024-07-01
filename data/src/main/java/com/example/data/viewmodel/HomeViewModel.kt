package com.example.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonData
import com.example.data.usecase.GetNextPokemonListUseCase
import com.example.data.usecase.GetPokemonListUseCase
import com.example.data.usecase.GetPokemonSpritesUseCase
import com.example.data.usecase.GetPreviousPokemonListUseCase
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
    private val getNextPokemonListUseCase: GetNextPokemonListUseCase,
    private val getPreviousPokemonListUseCase : GetPreviousPokemonListUseCase
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

    private val _previousPokemonListUrlState: MutableStateFlow<String> = MutableStateFlow(EMPTY_STRING)
    private val previousPokemonListUrl: StateFlow<String> = _previousPokemonListUrlState.asStateFlow()

    private val _pokemonDataSelectedState: MutableStateFlow<PokemonData?> = MutableStateFlow(null)
    val pokemonDataSelected: StateFlow<PokemonData?> = _pokemonDataSelectedState.asStateFlow()


    private val _loadNextPokemonListState : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadNextPokemonList : StateFlow<Boolean> =  _loadNextPokemonListState.asStateFlow()

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

    private fun setPreviousPokemonListUrl(url: String) {
        _previousPokemonListUrlState.value = url
    }

    fun getPokemonList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getPokemonListUseCase.invoke()
            if (response?.results?.isNotEmpty() == true) {
                if (response.next.isNullOrEmpty() || response.next == "null"){
                    setNextPokemonListUrl(EMPTY_STRING)
                }
                else{
                    setNextPokemonListUrl(response.next)
                }

                if (response.previous.isNullOrEmpty() || response.previous == "null"){
                    setPreviousPokemonListUrl(EMPTY_STRING)
                }
                else{
                    setPreviousPokemonListUrl(response.previous)
                }
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
            val offset = getOffset(nextPokemonListUrl.value)
            viewModelScope.launch(Dispatchers.IO) {
                val response = getNextPokemonListUseCase.invoke(offset,20)
                if (response?.results?.isNotEmpty() == true) {
                    if (response.next.isEmpty() || response.next == "null"){
                        setNextPokemonListUrl(EMPTY_STRING)
                    }
                    else{
                        setNextPokemonListUrl(response.next)
                    }

                    if (response.previous.isEmpty() || response.previous == "null"){
                        setPreviousPokemonListUrl(EMPTY_STRING)
                    }
                    else{
                        setPreviousPokemonListUrl(response.previous)
                    }
                    setPokemonList(response.results)
                }
            }.invokeOnCompletion {
                if (pokemonList.value.isNotEmpty()) {
                    getSprites()
                }
            }
        }
    }

    fun getPreviousPokemonItems() {
        if (previousPokemonListUrl.value != EMPTY_STRING) {
            val offset = getOffset(previousPokemonListUrl.value)
            viewModelScope.launch(Dispatchers.IO) {
                val response = getNextPokemonListUseCase.invoke(offset,20)
                if (response?.results?.isNotEmpty() == true) {
                    if (response.next.isEmpty() || response.next == "null"){
                        setNextPokemonListUrl(EMPTY_STRING)
                    }
                    else{
                        setNextPokemonListUrl(response.next)
                    }

                    if (response.previous.isEmpty() || response.previous == "null"){
                        setPreviousPokemonListUrl(EMPTY_STRING)
                    }
                    else{
                        setPreviousPokemonListUrl(response.previous)
                    }
                    setPokemonList(response.results)
                }
            }.invokeOnCompletion {
                if (pokemonList.value.isNotEmpty()) {
                    getSprites()
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

    private fun getOffset(url:String): Int {
        val parameters: String = url.split("?")[1]
        val numbers = parameters.split("&")
        return numbers[0].replace("offset=", EMPTY_STRING).toInt()
    }
}