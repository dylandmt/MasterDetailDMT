package com.example.masterdetaildmt.components.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.masterdetaildmt.R

@Composable
fun LoadingComponent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(0.2F)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.loading),
            fontSize = dimensionResource(id = R.dimen.master_details_20_dp).value.sp
        )
    }
}