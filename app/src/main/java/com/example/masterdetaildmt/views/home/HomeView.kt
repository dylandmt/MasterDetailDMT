package com.example.masterdetaildmt.views.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.data.viewmodel.MainViewModel
import com.example.masterdetaildmt.components.custom.PokemonItem
import org.koin.androidx.compose.inject

class HomeView {

    @Composable
    fun getInstance(){
        val mainViewModel : MainViewModel by inject()

        mainViewModel.getPokemonList()
        val pokemonList = mainViewModel.pokemonListDisplay.collectAsState().value
        if (pokemonList.isNotEmpty()){
            LazyColumn {
                items(pokemonList){ pokemon ->
                    PokemonItem(data = pokemon)
                }
            }
        }
    }
}