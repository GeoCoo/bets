package com.betsson.interviewtest.helpers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier
        .testTag("loadingIndicator")
        .background(Color.Transparent)
        .fillMaxSize()
        .padding(vertical = 8.dp)
        .zIndex(20f),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}