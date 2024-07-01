package com.example.masterdetaildmt.components.custom

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.masterdetaildmt.R
import com.example.masterdetaildmt.utils.Constants


@Composable
fun AnimatedButton(
    iconPainter: Painter = painterResource(R.drawable.arrow_down),
    onClick : () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition("")
    val animatedBackground by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Red.copy(0.2F),
        animationSpec = infiniteRepeatable(
            animation = tween(Constants.DEFAULT_ANIMATION_TIME, easing = LinearEasing),
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
                onClick.invoke()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = iconPainter, contentDescription = ""
        )
    }

}