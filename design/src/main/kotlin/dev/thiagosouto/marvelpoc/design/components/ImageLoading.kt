package dev.thiagosouto.marvelpoc.design.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun ImageLoading(
    modifier: Modifier = Modifier,
    url: String,
    height: Dp,
    contentDescription: String = "",
    contentScale: ContentScale
) {
    SubcomposeAsyncImage(
        model = url,
        loading = { Loading(modifier = modifier, heightIn = height) },
        modifier = modifier
            .height(height),
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}