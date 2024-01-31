package dev.thiagosouto.marvelpoc.design.components

import androidx.compose.foundation.Image
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.compose.painterResource
import dev.thiagosouto.marvelpoc.design.MR
/**
 * Component for favorite toggle from the design system
 */
@Composable
fun FavoriteToggle(
    isFavorite: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val painter = painterResource(MR.images.ic_favorite)

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            onCheckedChange(it)
        }
    ) {
        Image(
            painter = painter,
            contentDescription = "",
        )
    }
}
