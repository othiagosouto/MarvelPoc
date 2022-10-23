package dev.thiagosouto.marvelpoc.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thiagosouto.marvelpoc.data.CharactersRepository
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.detail.domain.DetailsViewStateMapper
import dev.thiagosouto.marvelpoc.shared.mvi.Presenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CharacterDetailsViewModel(
    private val repository: CharactersRepository,
    private val detailsViewStateMapper: DetailsViewStateMapper
) : ViewModel(),
    Presenter<Intent, DetailsViewState> {

    override fun process(intent: Intent) {
        processIntent(intent)
    }

    private val _state: MutableStateFlow<DetailsViewState> = MutableStateFlow(DetailsViewState.Idle)
    val state: StateFlow<DetailsViewState> = _state.asStateFlow()

    private fun processIntent(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is Intent.OpenScreen -> {
                    process(Intent.Internal.LoadScreen(intent.characterId))
                }
                is Intent.Internal.LoadScreen -> {
                    _state.tryEmit(DetailsViewState.Loading)
                    val details = repository.fetchCharacterDetails(intent.characterId.toString())
                    _state.tryEmit(
                        detailsViewStateMapper.apply(
                            DetailsViewStateMapper.Input(
                                name = details.name,
                                description = details.description,
                                imageUrl = details.imageUrl,
                                comics = details.comics
                            )
                        )
                    )
                }
                is Intent.CloseScreen -> _state.tryEmit(DetailsViewState.Closed)
            }
        }
    }
}
