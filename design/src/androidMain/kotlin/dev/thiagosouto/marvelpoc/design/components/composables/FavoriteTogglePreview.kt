package dev.thiagosouto.marvelpoc.design.components.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun FavoriteTogglePreview1() {
    FavoriteToggle( isFavorite = true){}
}

@Preview
@Composable
fun FavoriteTogglePreview2() {
    FavoriteToggle( isFavorite = false){}
}
