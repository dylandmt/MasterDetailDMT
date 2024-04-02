package com.example.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datamodels.Pokemon
import com.example.data.datamodels.PokemonData
import com.example.data.usecase.GetPokemonListUseCase
import com.example.data.usecase.GetPokemonSpritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel (private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonSpritesUseCase: GetPokemonSpritesUseCase) : ViewModel() {
    private val _pokemonListState : MutableStateFlow<ArrayList<Pokemon>> = MutableStateFlow(
        arrayListOf()
    )
    private val pokemonList : StateFlow<ArrayList<Pokemon>> = _pokemonListState.asStateFlow()

    private val _pokemonListDisplayState : MutableStateFlow<ArrayList<PokemonData>> = MutableStateFlow(
        arrayListOf()
    )
    val pokemonListDisplay : StateFlow<ArrayList<PokemonData>> = _pokemonListDisplayState.asStateFlow()

    private fun setPokemonList(pokemonList : ArrayList<Pokemon>){
        _pokemonListState.value = pokemonList
    }

    fun setPokemonDisplayList(pokemonList : ArrayList<PokemonData>){
        _pokemonListDisplayState.value = pokemonList
    }
    fun getPokemonList(){
        viewModelScope.launch (Dispatchers.IO){
            val response = getPokemonListUseCase.invoke()
            if(response?.results?.isNotEmpty() == true){
                setPokemonList(response.results)
                response.results[0].url.takeLast(2).replace("/","")
            }
        }.invokeOnCompletion {
            getSprites()
        }
    }

    fun getSprites(){
        viewModelScope.launch (Dispatchers.IO){
            var tempPokemonDisplayList : ArrayList<PokemonData> = arrayListOf()
            pokemonList.value.forEach {
                val sprites = getPokemonSpritesUseCase.invoke(it.url.takeLast(2).replace("/",""))
                sprites?.sprites?.let {sprite ->
                    tempPokemonDisplayList.add(PokemonData(name = it.name, urlImage = sprite.frontDefault))

                }
            }

            setPokemonDisplayList(tempPokemonDisplayList)
        }

    }
}