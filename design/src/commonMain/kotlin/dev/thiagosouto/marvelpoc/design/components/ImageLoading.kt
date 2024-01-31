package dev.thiagosouto.marvelpoc.design.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageScope

/**
 * Component for presenting a loading as placeholder and the image when the download is finished
 */
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
        contentDescription = contentDescription,
        imageLoader = ImageLoader.Builder(LocalPlatformContext.current).build(),
        loading =  { Loading(modifier = modifier, heightIn = height) },
        modifier = modifier
            .height(height),
        contentScale = contentScale,
    )
}
