package dev.thiagosouto.marvelpoc.design.components

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.thiagosouto.marvelpoc.design.R


/**
 * Component for favorite toggle from the design system
 */
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun FavoriteToggle(
    isFavorite: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    var atEnd by remember { mutableStateOf(isFavorite) }
    val image = AnimatedImageVector.animatedVectorResource(id = R.drawable.heart_animation)
    val painter = rememberAnimatedVectorPainter(
        image,
        atEnd
    )
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            atEnd = it
            onCheckedChange(it)
        }
    ) {
        Image(
            painter = painter,
            contentDescription = "",
        )
    }
}
