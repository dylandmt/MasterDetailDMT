package com.example.masterdetaildmt.components.custom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.masterdetaildmt.utils.Constants


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
            showRightActions.value = false
        }

        NavigationItem.DetailsView.route -> {
            showBackArrow.value = true
            showRightActions.value = true
        }

        else -> {
            showBackArrow.value = false
        }
    }
    /*Row(modifier = Modifier
        .height(dimensionResource(id = R.dimen.master_details_50_dp))
        .fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.3F)
                .background(androidx.compose.ui.graphics.Color.Red)
        ) {

        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6F)
                .background(androidx.compose.ui.graphics.Color.Green)
        ) {
            Text(text = title)
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(androidx.compose.ui.graphics.Color.Red)
        ) {

        }
    }*/
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
                        if (isFavorite.value) {
                            Constants.REMOVE_FAVORITE_POKEMON_ACTION
                        } else {
                            Constants.ADD_NEW_FAVORITE_POKEMON_ACTION
                        }
                    )
                    isFavorite.value = !isFavorite.value
                }) {
                    Icon(
                        painter = painterResource(
                            id = if (isFavorite.value) {
                                R.drawable.star_filled
                            } else {
                                R.drawable.star_outline
                            }
                        ),
                        contentDescription = "Leer después"
                    )
                }
            }
        }
    )
}