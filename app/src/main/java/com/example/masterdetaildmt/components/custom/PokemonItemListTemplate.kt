package com.example.masterdetaildmt.components.custom

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.data.datamodels.PokemonData
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.utils.Constants.Companion.ADD_NEW_FAVORITE_POKEMON_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.REMOVE_FAVORITE_POKEMON_ACTION

@Composable
fun PokemonItemListTemplate(
    pokemon: PokemonData,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: (String) -> Unit
) {
    val isFavorite = remember { mutableStateOf(pokemon.isFavorite) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = dimensionResource(id = R.dimen.master_details_2_dp),
                color = colorResource(id = R.color.poke_red),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.master_details_10_dp))
            )
            .clip(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.master_details_10_dp))
            )
            .padding(
                horizontal = dimensionResource(id = R.dimen.master_details_10_dp),
                vertical = dimensionResource(
                    id = R.dimen.master_details_10_dp
                )
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
            painter = painterResource(
                id = if (isFavorite.value) {
                    R.drawable.star_filled
                } else {
                    R.drawable.star_outline
                }
            ),
            tint = colorResource(id = R.color.poke_yellow_secondary),
            contentDescription = "Favorite",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.master_details_30_dp))
                .clickable {
                    onSecondaryAction.invoke(
                        if (isFavorite.value) {
                            REMOVE_FAVORITE_POKEMON_ACTION
                        } else {
                            ADD_NEW_FAVORITE_POKEMON_ACTION
                        }
                    )
                    isFavorite.value = !isFavorite.value
                }
        )
    }
}