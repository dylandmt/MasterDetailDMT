package com.example.masterdetaildmt.components.custom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.navigation.NavigationItem
import com.example.masterdetaildmt.utils.Constants.Companion.ADD_NEW_FAVORITE_POKEMON_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.CUSTOM_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.REMOVE_FAVORITE_POKEMON_ACTION


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String = stringResource(id = R.string.no_data),
    currentView: String = "",
    defaultToggleActionValue: Boolean = false,
    onNavigationAction: () -> Unit,
    onSelectionAction: (String) -> Unit
) {
    val showBackArrow = remember { mutableStateOf(false) }
    val showRightActions = remember { mutableStateOf(false) }
    val isFavorite = remember { mutableStateOf(false) }
    isFavorite.value = defaultToggleActionValue

    when (currentView) {
        NavigationItem.HomeView.route -> {
            showBackArrow.value = false
            showRightActions.value = true
        }

        NavigationItem.DetailsView.route -> {
            showBackArrow.value = true
            showRightActions.value = true
        }

        NavigationItem.LocationsView.route -> {
            showBackArrow.value = true
            showRightActions.value = false
        }

        else -> {
            showBackArrow.value = false
        }
    }
    TopAppBar(
        navigationIcon = {
            if (showBackArrow.value) {
                IconButton(onClick = { onNavigationAction.invoke() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }
            }
        },
        title = { Text(text = title) },
        actions = {
            if (showRightActions.value){
                IconButton(onClick = {
                    onSelectionAction.invoke(
                        if (currentView == NavigationItem.HomeView.route){
                            CUSTOM_ACTION
                        }
                        else if (isFavorite.value) {
                            REMOVE_FAVORITE_POKEMON_ACTION
                        } else {
                            ADD_NEW_FAVORITE_POKEMON_ACTION
                        }
                    )
                    isFavorite.value = !isFavorite.value
                }) {

                    if (currentView == NavigationItem.HomeView.route){
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = ""
                        )
                    }
                    else {
                        Icon(
                            painter = painterResource(
                                id = if (isFavorite.value) {
                                    R.drawable.star_filled
                                } else {
                                    R.drawable.star_outline
                                }
                            ),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    )
}