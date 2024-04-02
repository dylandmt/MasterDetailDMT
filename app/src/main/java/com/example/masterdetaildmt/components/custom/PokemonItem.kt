package com.example.masterdetaildmt.components.custom

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.data.datamodels.PokemonData
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.utils.Constants.Companion.DISPLAY_IMAGE_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.DISPLAY_INITIALS_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.DISPLAY_PLACEHOLDER_ACTION

@Composable
fun PokemonItem(data: PokemonData) {
    var action by rememberSaveable { mutableStateOf(DISPLAY_PLACEHOLDER_ACTION) }
    action = determineAction(data.name, data.urlImage)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (action) {
            DISPLAY_INITIALS_ACTION -> {
                Column(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.master_details_100_dp))
                        .background(
                            Color.Red,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = data.name.substring(0, 2).uppercase())
                }
            }

            DISPLAY_PLACEHOLDER_ACTION -> {

                Image(
                    painter = painterResource(id = R.drawable.person_placeholder_icon),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.master_details_100_dp)),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = ""
                )
            }

            DISPLAY_IMAGE_ACTION -> {
                Column(
                    modifier = Modifier
                        .background(Color.Green, CircleShape)
                        .size(
                            dimensionResource(id = R.dimen.master_details_100_dp)
                        )
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(data.urlImage),
                        contentDescription = null,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.master_details_100_dp))
                    )
                }
            }

            else -> {}
        }
    }

}

private fun determineAction(name: String, url: String): String {
    return if (url.isNullOrEmpty()) {
        if (!name.isNullOrEmpty()) {
            DISPLAY_INITIALS_ACTION
        } else {
            DISPLAY_PLACEHOLDER_ACTION
        }
    } else {
        DISPLAY_IMAGE_ACTION
    }
}