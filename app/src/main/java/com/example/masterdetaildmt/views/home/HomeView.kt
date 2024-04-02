package com.example.masterdetaildmt.views.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.data.viewmodel.HomeViewModel
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.components.custom.PokemonItem
import org.koin.androidx.compose.inject

class HomeView {

    @Composable
    fun getInstance(){
        val homeViewModel : HomeViewModel by inject()
        homeViewModel.getPokemonList()
        val pokemonList = homeViewModel.pokemonListDisplay.collectAsState().value
        if (pokemonList.isNotEmpty()){
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(pokemonList) { pokemon ->
                        PokemonItem(data = pokemon)
                    }
                },
                modifier = Modifier.fillMaxSize())
        }
    }
}