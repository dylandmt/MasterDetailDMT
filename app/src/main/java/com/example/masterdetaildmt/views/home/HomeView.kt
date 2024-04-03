package com.example.masterdetaildmt.views.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import com.example.data.viewmodel.HomeViewModel
import com.example.masterdetaildmt.R
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
        LaunchedEffect(Unit) {
            if (pokemonList.isEmpty()) {
                homeViewModel.getPokemonList()
            }
        }

        if (pokemonList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                pokemonListDisplay.forEach { pokemon ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().border(
                            width = dimensionResource(id = R.dimen.master_details_2_dp),
                            color = Color.Gray
                        )
                    ) {
                        PokemonItem(data = pokemon) {
                            homeViewModel.setPokemonDataSelected(it)
                            navController.navigate(NavigationItem.DetailsView.route)
                        }
                        Text(
                            text = pokemon.name,
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.master_details_2_dp))
                        )
                    }
                }
            }
        }

    }
}