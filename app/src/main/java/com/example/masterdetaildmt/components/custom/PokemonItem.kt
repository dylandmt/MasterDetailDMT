package com.example.masterdetaildmt.components.custom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.data.datamodels.PokemonData
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.utils.Constants.Companion.DISPLAY_IMAGE_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.DISPLAY_INITIALS_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.DISPLAY_PLACEHOLDER_ACTION

@Composable
fun PokemonItem(
    data: PokemonData,
    borderColor: Color = colorResource(id = R.color.poke_blue),
    fillColor: Color = colorResource(id = R.color.poke_yellow),
    size: Dp = dimensionResource(id = R.dimen.master_details_100_dp),
    horizontalMargin: Dp = dimensionResource(id = R.dimen.master_details_0_dp),
    verticalMargin: Dp = dimensionResource(id = R.dimen.master_details_0_dp),
    onClick: (PokemonData) -> Unit
) {
    var action by rememberSaveable { mutableStateOf(DISPLAY_PLACEHOLDER_ACTION) }
    action = determineAction(name = data.name, url = data.urlImage)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = verticalMargin, horizontal = horizontalMargin)
    ) {
        Column(
            modifier = Modifier
                .background(fillColor, CircleShape)
                .size(size)
                .border(BorderStroke(2.dp, borderColor), shape = CircleShape)
                .clickable {
                    onClick.invoke(data)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (action) {
                DISPLAY_INITIALS_ACTION -> {
                    Text(
                        text = data.name.substring(0, 2).uppercase(),
                        fontSize = 40.sp
                    )
                }

                DISPLAY_PLACEHOLDER_ACTION -> {

                    Image(
                        painter = painterResource(id = R.drawable.person_placeholder_icon),
                        modifier = Modifier.size(size),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = ""
                    )
                }

                DISPLAY_IMAGE_ACTION -> { 
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data.urlImage)
                            .size(coil.size.Size.ORIGINAL)
                            .build()
                    )

                    when (painter.state) {
                        is AsyncImagePainter.State.Success -> {
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier.size(size)
                            )
                        }

                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.master_details_20_dp))
                            )
                        }

                        is AsyncImagePainter.State.Error -> {
                            Text(text = data.name.substring(0).uppercase())
                        }
                        else -> {}
                    }
                    Text(text = data.name)
                }

                else -> {}
            }
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