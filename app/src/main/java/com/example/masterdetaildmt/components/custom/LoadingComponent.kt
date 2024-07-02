package com.example.masterdetaildmt.components.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingComponent(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray.copy(0.2F)),
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator()
    }
}