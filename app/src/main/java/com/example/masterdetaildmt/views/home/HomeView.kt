package com.example.masterdetaildmt.views.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.data.viewmodel.HomeViewModel
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.components.custom.AnimatedButton
import com.example.masterdetaildmt.components.custom.PokemonItemListTemplate
import com.example.masterdetaildmt.navigation.NavigationItem
import org.koin.androidx.compose.inject

class HomeView {

    @Composable
    fun getInstance(navController: NavHostController) {
        val homeViewModel: HomeViewModel by inject()
        val pokemonList = homeViewModel.pokemonList.collectAsState().value
        val pokemonListDisplay = homeViewModel.pokemonListDisplay.collectAsState().value
        val loadNextPokemonList = homeViewModel.loadNextPokemonList.collectAsState().value


        val scrollState = rememberLazyListState()
        val endOfListReached by remember {
            derivedStateOf {
                scrollState.isScrolledToEnd()
            }
        }
        LaunchedEffect(Unit) {
            if (pokemonList.isEmpty()) {
                homeViewModel.getPokemonList()
            }
        }


        if (pokemonList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = scrollState
            ) {
                item {
                    AnimatedButton(
                        iconPainter = painterResource(
                            id = R.drawable.arrow_up
                        )
                    ) {
                        homeViewModel.getPreviousPokemonItems()
                    }
                }
                items(pokemonListDisplay) {pokemon ->
                   PokemonItemListTemplate(pokemon,
                       onPrimaryAction = {
                           homeViewModel.setPokemonDataSelected(pokemon)
                           navController.navigate(NavigationItem.DetailsView.route)
                       },
                       onSecondaryAction = {

                       })
                }
                item {
                    AnimatedButton(
                        iconPainter = painterResource(
                            id = R.drawable.arrow_down
                        )
                    ) {
                        homeViewModel.getNextPokemonItems()
                    }
                }
            }
        }

    }

    private fun LazyListState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

}