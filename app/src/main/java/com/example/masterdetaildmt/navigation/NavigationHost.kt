package com.example.masterdetaildmt.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.masterdetaildmt.views.details.DetailsView
import com.example.masterdetaildmt.views.home.HomeView

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.HomeView.route,
    context: Context
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.HomeView.route){
            HomeView().getInstance(navController,context)
        }
        composable(NavigationItem.DetailsView.route){
            DetailsView().getInstance(navController)
        }
    }
}