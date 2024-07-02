package com.example.masterdetaildmt

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import com.example.masterdetaildmt.components.custom.CustomTopBar
import com.example.masterdetaildmt.navigation.NavigationHost
import com.example.masterdetaildmt.navigation.NavigationItem
import com.example.masterdetaildmt.utils.Constants
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val currentView = remember { mutableStateOf("") }
            val homeViewModel: HomeViewModel by inject()
            val pokemonSelected: PokemonData? = homeViewModel.pokemonDataSelected.collectAsState().value
            val toggleCurrentValue = remember { mutableStateOf(false) }


            LaunchedEffect(navBackStackEntry) {
                currentView.value = navBackStackEntry?.destination?.route.toString()
                when(navBackStackEntry?.destination?.route){
                   NavigationItem.HomeView.route -> {
                       Log.d("IS IN","HOME")
                       toggleCurrentValue.value = false

                   }
                   NavigationItem.DetailsView.route -> {
                       Log.d("IS IN","DETAILS")
                       toggleCurrentValue.value = pokemonSelected!=null && pokemonSelected.isFavorite
                   }
                   else ->{}
               }
            }

            Scaffold(
                topBar = {
                    CustomTopBar(title = stringResource(id = R.string.welcome_top_bar),
                        currentView = currentView.value,
                        defaultToggleActionValue = toggleCurrentValue.value,
                        onNavigationAction = {navController.popBackStack()},
                        onSelectionAction = {customAction ->
                            when(customAction){
                                Constants.ADD_NEW_FAVORITE_POKEMON_ACTION -> {
                                    homeViewModel.pokemonDataSelected.value?.let {
                                        homeViewModel.addNewFavoritePokemon(
                                            it
                                        )
                                    }
                                }
                                Constants.REMOVE_FAVORITE_POKEMON_ACTION ->{
                                    homeViewModel.pokemonDataSelected.value?.let {
                                        homeViewModel.removeFavoritePokemon(
                                            it
                                        )
                                    }
                                }
                                else -> {}
                            }
                        })
                }
            ) { innerPadding ->
                NavigationHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    context = applicationContext
                )
            }
        }
    }
}
