package com.example.masterdetaildmt.components.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.data.datamodels.PokemonData
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.navigation.NavigationItem

@Composable
fun PokemonItemListTemplate(
    pokemon: PokemonData,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = dimensionResource(id = R.dimen.master_details_2_dp),
                color = Color.Gray
            )
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.9F)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PokemonItem(
                    data = pokemon,
                    verticalMargin = dimensionResource(id = R.dimen.master_details_10_dp),
                    horizontalMargin = dimensionResource(id = R.dimen.master_details_10_dp)
                ) {
                    onPrimaryAction.invoke()
                }
                Text(
                    text = pokemon.name,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.master_details_2_dp))
                )
            }
        }
        Icon(
            painter = painterResource(id = R.drawable.star_outline),
            contentDescription = "Favorite",
            modifier = Modifier.size(dimensionResource(id = R.dimen.master_details_30_dp)).clickable {
                onSecondaryAction.invoke()
            }
        )
    }
}