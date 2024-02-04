package dev.thiagosouto.marvelpoc.design.components.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Component for favorite toggle from the design system
 */
@Composable
fun FavoriteToggle(
    isFavorite: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            onCheckedChange(it)
        }
    ) {

        AnimatedContent(
            targetState = isFavorite,
            transitionSpec = {
                fadeIn(animationSpec = tween(1_000, 150)) togetherWith
                        fadeOut(animationSpec = tween(150))
            }
        ){ isFavorite2 ->
            when (isFavorite2) {
                true -> Icon(
                    imageVector = Icons.Outlined.Favorite,
                    tint = Color.Red,
                    contentDescription = "",
                )

                false -> Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    tint = Color.Red,
                    contentDescription = "",
                )
            }
        }
    }
}
