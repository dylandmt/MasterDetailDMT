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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.data.viewmodel.DetailsViewModel
import com.example.data.viewmodel.HomeViewModel
import com.example.masterdetaildmt.R
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
            PokemonItem(
                data = pokemonDataSelected!!,
                size = dimensionResource(id = R.dimen.master_details_300_dp)
            ) {

            }


            val infiniteTransition = rememberInfiniteTransition("")
            val animatedBackground by infiniteTransition.animateColor(
                initialValue = Color.Red,
                targetValue = Color.Red.copy(0.2F),
                animationSpec = infiniteRepeatable(
                    animation = tween(DEFAULT_ANIMATION_TIME, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = ""
            )

            Column(
                modifier = Modifier
                    .background(color = animatedBackground)
                    .border(width = dimensionResource(R.dimen.master_details_2_dp), Color.Black)
                    .height(dimensionResource(R.dimen.master_details_20_dp))
                    .fillMaxWidth()
                    .clickable {
                        detailsViewModel.setShowSprites(!showSprites)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        id = if (showSprites) {
                            R.drawable.arrow_up
                        } else {
                            R.drawable.arrow_down
                        }
                    ), contentDescription = ""
                )
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
    private fun NoDataToShowMessage(){
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
}