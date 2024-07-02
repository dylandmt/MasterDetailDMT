package com.example.masterdetaildmt.views.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.data.datamodels.PokemonData
import com.example.data.viewmodel.DetailsViewModel
import com.example.data.viewmodel.HomeViewModel
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.components.custom.AnimatedButton
import com.example.masterdetaildmt.components.custom.CustomTopBar
import com.example.masterdetaildmt.components.custom.PokemonItem
import com.example.masterdetaildmt.utils.Constants.Companion.DEFAULT_ANIMATION_TIME
import org.koin.androidx.compose.inject

class DetailsView {
    @Composable
    fun getInstance(navController: NavHostController) {
        val homeViewModel: HomeViewModel by inject()
        val detailsViewModel: DetailsViewModel by inject()
        val pokemonDataSelected = homeViewModel.pokemonDataSelected.collectAsState().value
        val spritesList = detailsViewModel.spritesList.collectAsState().value
        val showSprites = detailsViewModel.showSprites.collectAsState().value
        val showDetails = detailsViewModel.showDetails.collectAsState().value
        pokemonDataSelected?.details?.sprites?.let {
            detailsViewModel.setSpritesList(pokemonSprites = it)
        }

        var animateBackgroundColor by remember {
            mutableStateOf(true)
        }
        LaunchedEffect(Unit) {
            animateBackgroundColor = spritesList.isNotEmpty()
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                PokemonItem(
                    data = pokemonDataSelected!!,
                    size = dimensionResource(id = R.dimen.master_details_300_dp)
                ) {
                    detailsViewModel.setShowDetails(true)
                }
                if (showDetails) {
                    PokemonDataContent(pokemonDataSelected) {
                        detailsViewModel.setShowDetails(false)
                    }
                }
            }

            AnimatedButton(
                iconPainter = painterResource(
                    id = if (showSprites) {
                        R.drawable.arrow_up
                    } else {
                        R.drawable.arrow_down
                    }
                )
            ) {
                detailsViewModel.setShowSprites(!showSprites)
            }

            AnimatedVisibility(
                visible = showSprites,
                enter = slideInVertically(
                    // Start the slide from 40 (pixels) above where the content is supposed to go, to
                    // produce a parallax effect
                    initialOffsetY = { -40 }
                ) + expandVertically(
                    expandFrom = Alignment.Top
                ) + scaleIn(
                    // Animate scale from 0f to 1f using the top center as the pivot point.
                    transformOrigin = TransformOrigin(0.5f, 0f)
                ) + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(targetScale = 1.2f)
            ) {
                if (spritesList.isEmpty()) {
                    NoDataToShowMessage()
                } else {
                    SpritesContent(spritesList)
                }
            }
        }
    }

    @Composable
    private fun NoDataToShowMessage() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "No data to show.")
        }
    }

    @Composable
    private fun SpritesContent(spritesList: ArrayList<String>) {
        spritesList.let {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(it) { sprite ->
                        AsyncImage(
                            model = sprite,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Composable
    private fun PokemonDataContent(
        pokemonData: PokemonData,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .background(Color.Black.copy(0.5F), CircleShape)
                .size(dimensionResource(id = R.dimen.master_details_300_dp))
                .border(BorderStroke(2.dp, Color.White), shape = CircleShape)
                .clickable { onClick.invoke() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.id, pokemonData.details.id),
                style = TextStyle(
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.master_details_20_sp).value.sp
                )
            )
            Text(
                text = stringResource(id = R.string.name, pokemonData.name),
                style = TextStyle(
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.master_details_20_sp).value.sp
                )
            )
            Text(
                text = stringResource(id = R.string.height, pokemonData.details.height),
                style = TextStyle(
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.master_details_20_sp).value.sp
                )
            )
            Text(
                text = stringResource(id = R.string.weight, pokemonData.details.weight),
                style = TextStyle(
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.master_details_20_sp).value.sp
                )
            )
        }
    }
}