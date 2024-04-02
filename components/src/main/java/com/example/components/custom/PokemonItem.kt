package com.example.components.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.components.R
import com.example.components.utils.Constants.Companion.DISPLAY_IMAGE_ACTION
import com.example.components.utils.Constants.Companion.DISPLAY_INITIALS_ACTION
import com.example.components.utils.Constants.Companion.DISPLAY_PLACEHOLDER_ACTION

@Composable
fun PokemonItem(name: String, url: String) {
    var action by rememberSaveable { mutableStateOf(DISPLAY_PLACEHOLDER_ACTION) }
    var painter = rememberAsyncImagePainter(url)
    when (painter.state) {
        is AsyncImagePainter.State.Loading -> {
            action = DISPLAY_PLACEHOLDER_ACTION
        }

        is AsyncImagePainter.State.Success -> {
            action = DISPLAY_IMAGE_ACTION
        }

        is AsyncImagePainter.State.Error -> {
            action = if (!name.isNullOrEmpty()) {
                DISPLAY_INITIALS_ACTION
            } else {
                DISPLAY_PLACEHOLDER_ACTION
            }
        }

        else -> {}
    }

    when (action) {
        DISPLAY_IMAGE_ACTION -> {
            Image(
                painter = painter,
                modifier = Modifier.size(dimensionResource(id = R.dimen.master_detail_100_dp)),
                contentScale = ContentScale.FillBounds,
                contentDescription = ""
            )
        }

        DISPLAY_INITIALS_ACTION -> {
            Column(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.master_detail_100_dp))
                    .background(
                        Color.Red
                    )
            ) {
                Text(text = name.substring(0, 1).uppercase())
            }
        }

        DISPLAY_PLACEHOLDER_ACTION -> {
            Image(
                painter = rememberAsyncImagePainter(painterResource(id = R.drawable.person_placeholder_icon)),
                modifier = Modifier.size(dimensionResource(id = R.dimen.master_detail_100_dp)),
                contentScale = ContentScale.FillBounds,
                contentDescription = ""
            )
        }

        else -> {}
    }

}
