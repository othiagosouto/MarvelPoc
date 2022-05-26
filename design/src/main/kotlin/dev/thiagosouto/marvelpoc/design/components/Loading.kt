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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import dev.thiagosouto.marvelpoc.design.R

@Composable
fun Loading(modifier: Modifier = Modifier, heightIn: Dp) {
    Box(
        modifier = modifier
            .heightIn(heightIn),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(dimensionResource(id = R.dimen.loading_page_size)))
    }
}

@Composable
fun LoadingPage() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val size = dimensionResource(id = R.dimen.loading_page_size)
        val loadingSize = dimensionResource(id = R.dimen.loading_page_size)
        Loading(modifier = Modifier.size(size), loadingSize)
    }
}
