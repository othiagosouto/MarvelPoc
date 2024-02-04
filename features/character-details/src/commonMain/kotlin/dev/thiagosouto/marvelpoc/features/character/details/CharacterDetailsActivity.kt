package dev.thiagosouto.marvelpoc.features.character.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.colorResource
import dev.thiagosouto.marvelpoc.design.components.LoadingPage
import dev.thiagosouto.marvelpoc.design.DesignSystemRes

@Composable
fun CharacterDetailsScreen(
    process: (Intent) -> Unit,
    viewState: DetailsViewState
) {
    when (viewState) {
        is DetailsViewState.Loading -> LoadingPage()
        is DetailsViewState.Loaded -> LoadedViewState(viewState, process)
        else -> Unit
    }
}

@Composable
private fun LoadedViewState(viewState: DetailsViewState.Loaded, process: (Intent) -> Unit) {
    Column {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = { process(Intent.CloseScreen) }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colorResource(DesignSystemRes.colors.white)
                    )
                }
            },
            backgroundColor = colorResource(DesignSystemRes.colors.colorPrimary),
            elevation = 2.dp
        )

        CharacterDetails(
            viewState.name,
            viewState.description,
            viewState.imageUrl,
            viewState.comics
        )
    }
}
