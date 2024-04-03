package com.example.masterdetaildmt.components.custom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.datamodels.PokemonData
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.utils.Constants.Companion.DISPLAY_IMAGE_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.DISPLAY_INITIALS_ACTION
import com.example.masterdetaildmt.utils.Constants.Companion.DISPLAY_PLACEHOLDER_ACTION

@Composable
fun PokemonItem(
    data: PokemonData,
    borderColor: Color = Color.Red,
    fillColor: Color = Color.Gray,
    size : Dp = dimensionResource(id = R.dimen.master_details_100_dp),
    onClick : (PokemonData) -> Unit
) {
    var action by rememberSaveable { mutableStateOf(DISPLAY_PLACEHOLDER_ACTION) }
    action = determineAction(name = data.name, url = data.urlImage)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                    Image(
                        painter = rememberAsyncImagePainter(data.urlImage),
                        contentDescription = null,
                        modifier = Modifier.size(size)
                    )
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