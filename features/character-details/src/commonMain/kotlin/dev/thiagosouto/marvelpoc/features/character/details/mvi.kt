package dev.thiagosouto.marvelpoc.features.character.details

import dev.thiagosouto.marvelpoc.domain.model.Comics

sealed class Intent {
    data class OpenScreen(val characterId: Long) : Intent()

    object CloseScreen : Intent()

    sealed class Internal : Intent() {
        data class LoadScreen(val characterId: Long) : Intent()
    }
}

sealed class DetailsViewState {

    data class Loaded(val name: String, val description: String, val imageUrl: String, val comics: List<Comics>) :
        DetailsViewState()

    object Loading : DetailsViewState()

    object Idle : DetailsViewState()

    object Closed: DetailsViewState()
}
