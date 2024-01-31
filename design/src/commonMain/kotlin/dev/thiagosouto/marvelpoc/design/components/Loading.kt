package dev.thiagosouto.marvelpoc.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Component for loading from the design system
 */
@Composable
fun Loading(modifier: Modifier = Modifier, heightIn: Dp) {
    Box(
        modifier = modifier
            .heightIn(heightIn),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(72.dp))
    }
}

/**
 * Component for loading page from the design system
 */
@Composable
fun LoadingPage(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val size = 72.dp
        val loadingSize = 72.dp
        Loading(modifier = Modifier.size(size), loadingSize)
    }
}
