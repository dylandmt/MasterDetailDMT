package com.example.masterdetaildmt.views.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.data.viewmodel.HomeViewModel
import com.example.masterdetaildmt.components.custom.PokemonItem
import com.example.masterdetaildmt.navigation.NavigationItem
import org.koin.androidx.compose.inject

class HomeView {

    @Composable
    fun getInstance(navController: NavHostController) {
        val homeViewModel: HomeViewModel by inject()
        val pokemonList = homeViewModel.pokemonList.collectAsState().value
        val pokemonListDisplay = homeViewModel.pokemonListDisplay.collectAsState().value

        val scrollState = rememberScrollState()
        val endReached by remember {
            derivedStateOf {
                scrollState.value == scrollState.maxValue
            }
        }
        LaunchedEffect(Unit) {
            if (pokemonList.isEmpty()) {
                homeViewModel.getPokemonList()
            }
        }
        if (endReached) {
            //homeViewModel.getNextPokemonItems()
            Log.d("JEJE", "ASDASD")
        }

        if (pokemonList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                pokemonListDisplay.forEach { pokemon ->
                    PokemonItem(data = pokemon) {
                        homeViewModel.setPokemonDataSelected(it)
                        navController.navigate(NavigationItem.DetailsView.route)
                    }
                }
            }
        }

    }
}