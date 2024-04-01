package com.example.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datamodels.Pokemon
import com.example.data.usecase.GetPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel (private val getPokemonListUseCase: GetPokemonListUseCase) : ViewModel() {

    private val _pokemonListState : MutableStateFlow<ArrayList<Pokemon>> = MutableStateFlow(
        arrayListOf()
    )
    val pokemonList : StateFlow<ArrayList<Pokemon>> = _pokemonListState.asStateFlow()

    fun setPokemonList(pokemonList : ArrayList<Pokemon>){
        _pokemonListState.value = pokemonList
    }
    fun getPokemonList(){
        viewModelScope.launch (Dispatchers.IO){
            val response = getPokemonListUseCase.invoke()
            if(response?.results?.isNotEmpty() == true){
                setPokemonList(response.results)
            }
        }
    }
}