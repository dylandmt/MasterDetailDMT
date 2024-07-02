package com.example.masterdetaildmt.views.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.data.di.provideDao
import com.example.data.localstorage.FavoritePokemonDatabase
import com.example.data.localstorage.FavoritePokemonEntity
import com.example.data.localstorage.LocalStorageDAO
import com.example.data.viewmodel.HomeViewModel
import com.example.data.viewmodel.LoadingComponentViewModel
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.components.custom.AnimatedButton
import com.example.masterdetaildmt.components.custom.LoadingComponent
import com.example.masterdetaildmt.components.custom.PokemonItemListTemplate
import com.example.masterdetaildmt.navigation.NavigationItem
import com.example.masterdetaildmt.utils.Constants.Companion.ADD_NEW_FAVORITE_POKEMON_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.REMOVE_FAVORITE_POKEMON_ACTION
import org.koin.androidx.compose.inject

class HomeView {

    @Composable
    fun getInstance(navController: NavHostController, context: Context) {
        val homeViewModel: HomeViewModel by inject()
        val pokemonList = homeViewModel.pokemonList.collectAsState().value
        val pokemonListDisplay = homeViewModel.pokemonListDisplay.collectAsState().value
        val showNextButton = homeViewModel.showNextButton.collectAsState().value
        val showPreviousButton = homeViewModel.showPreviousButton.collectAsState().value

         val loadingComponentViewModel: LoadingComponentViewModel by inject()
        val showLoading = loadingComponentViewModel.showLoading.collectAsState().value

        val scrollState = rememberLazyListState()
        /*val endOfListReached by remember {
            derivedStateOf {
                scrollState.isScrolledToEnd()
            }
        }*/
        /*LaunchedEffect(pokemonList.isEmpty()) {
            if (pokemonList.isEmpty()) {
                loadingComponentViewModel.showLoading(true)
            }
        }*/


        if (pokemonList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = scrollState,
                    userScrollEnabled = !showLoading
                ) {
                    if (showPreviousButton && !showLoading) {
                        item {
                            AnimatedButton(
                                iconPainter = painterResource(
                                    id = R.drawable.arrow_up
                                )
                            ) {
                                homeViewModel.getPreviousPokemonItems()
                            }
                        }
                    }
                    items(pokemonListDisplay) { pokemon ->
                        PokemonItemListTemplate(pokemon,
                            onPrimaryAction = {
                                homeViewModel.setPokemonDataSelected(pokemon)
                                navController.navigate(NavigationItem.DetailsView.route)
                            },
                            onSecondaryAction = { customAction ->
                                when (customAction) {
                                    ADD_NEW_FAVORITE_POKEMON_ACTION -> {
                                        homeViewModel.addNewFavoritePokemon(pokemon)
                                    }

                                    REMOVE_FAVORITE_POKEMON_ACTION -> {
                                        homeViewModel.removeFavoritePokemon(pokemon)
                                    }

                                    else -> {}
                                }
                            })
                    }

                    if (showNextButton && !showLoading) {
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
    }

    private fun LazyListState.isScrolledToEnd() =
        layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

}