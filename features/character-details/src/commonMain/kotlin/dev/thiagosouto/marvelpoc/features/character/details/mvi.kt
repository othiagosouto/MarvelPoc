package dev.thiagosouto.marvelpoc.features.character.details

import dev.thiagosouto.marvelpoc.domain.model.Comics
import dev.thiagosouto.marvelpoc.support.presentation.ViewState

sealed class Intent {
    data class OpenScreen(val characterId: Long) : Intent()

    data object CloseScreen : Intent()

    sealed class Internal : Intent() {
        data class LoadScreen(val characterId: Long) : Intent()
    }
}

sealed class DetailsViewState: ViewState {

    data class Loaded(
        val name: String,
        val description: String,
        val imageUrl: String,
        val comics: List<Comics>
    ) :
        DetailsViewState()

    data object Loading : DetailsViewState()

    data object Idle : DetailsViewState()

    data object Closed : DetailsViewState()
}
