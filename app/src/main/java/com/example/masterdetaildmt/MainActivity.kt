package com.example.masterdetaildmt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.datamodels.PokemonData
import com.example.data.viewmodel.HomeViewModel
import com.example.data.viewmodel.LoadingComponentViewModel
import com.example.masterdetaildmt.components.custom.CustomTopBar
import com.example.masterdetaildmt.components.custom.LoadingComponent
import com.example.masterdetaildmt.navigation.NavigationHost
import com.example.masterdetaildmt.navigation.NavigationItem
import com.example.masterdetaildmt.services.LocationBackgroundService
import com.example.masterdetaildmt.utils.Constants.Companion.ADD_NEW_FAVORITE_POKEMON_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.CUSTOM_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.REMOVE_FAVORITE_POKEMON_ACTION
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val currentView = remember { mutableStateOf("") }
            val homeViewModel: HomeViewModel by inject()

            val pokemonSelected: PokemonData? =
                homeViewModel.pokemonDataSelected.collectAsState().value
            val toggleCurrentValue = remember { mutableStateOf(false) }

            val loadingComponentViewModel: LoadingComponentViewModel by inject()
            val showLoading = loadingComponentViewModel.showLoading.collectAsState().value

            LaunchedEffect(navBackStackEntry) {
                currentView.value = navBackStackEntry?.destination?.route.toString()
                when (navBackStackEntry?.destination?.route) {
                    NavigationItem.HomeView.route -> {
                        toggleCurrentValue.value = false
                    }

                    NavigationItem.DetailsView.route -> {
                        toggleCurrentValue.value =
                            pokemonSelected != null && pokemonSelected.isFavorite
                    }

                    else -> {}
                }
            }


            Scaffold(
                topBar = {
                    CustomTopBar(title = getTopBarTitle(currentView.value,pokemonSelected),
                        currentView = currentView.value,
                        defaultToggleActionValue = toggleCurrentValue.value,
                        onNavigationAction = { navController.popBackStack() },
                        onSelectionAction = { customAction ->
                            when (customAction) {
                                ADD_NEW_FAVORITE_POKEMON_ACTION -> {
                                    homeViewModel.pokemonDataSelected.value?.let {
                                        homeViewModel.addNewFavoritePokemon(
                                            it
                                        )
                                    }
                                }

                                REMOVE_FAVORITE_POKEMON_ACTION -> {
                                    homeViewModel.pokemonDataSelected.value?.let {
                                        homeViewModel.removeFavoritePokemon(
                                            it
                                        )
                                    }
                                }

                                CUSTOM_ACTION -> {
                                    navController.navigate(NavigationItem.LocationsView.route)
                                }

                                else -> {}
                            }
                        })
                }
            ) { innerPadding ->
                if (showLoading) {
                    LoadingComponent()
                }
                NavigationHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    context = applicationContext
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {

            Intent(applicationContext, LocationBackgroundService::class.java).also {
                it.action = LocationBackgroundService.Actions.START.toString()
                applicationContext.startService(it)
            }
        }
        catch (e: Exception){
            Log.d("Excption",e.message.toString())
        }

    }

    @Composable
    private fun getTopBarTitle(currentView:String, pokemonSelected : PokemonData?):String {
        val title: String = when (currentView) {
            NavigationItem.HomeView.route -> {
                stringResource(id = R.string.welcome_top_bar)
            }

            NavigationItem.LocationsView.route -> {
                stringResource(id = R.string.locations_title)
            }

            else -> {
                pokemonSelected?.name.toString().uppercase()
            }
        }
        return title
    }
}
