package dev.thiagosouto.marvelpoc.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thiagosouto.domain.Mapper
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.CharacterDetailsService
import dev.thiagosouto.marvelpoc.data.Dispatchers
import dev.thiagosouto.marvelpoc.detail.domain.DetailsViewStateMapper
import dev.thiagosouto.marvelpoc.support.presentation.Presenter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CharacterDetailsViewModel(
    private val repository: CharacterDetailsService<CharacterDetails>,
    private val detailsViewStateMapper: Mapper<DetailsViewStateMapper.Input, DetailsViewState>,
    private val dispatchers: Dispatchers
) : ViewModel(),
    Presenter<Intent, DetailsViewState> {

    override fun process(intent: Intent) {
        processIntent(intent)
    }

    private val _state: MutableStateFlow<DetailsViewState> = MutableStateFlow(DetailsViewState.Idle)
    val state: StateFlow<DetailsViewState> = _state.asStateFlow()

    private fun processIntent(intent: Intent) {
        viewModelScope.launch(dispatchers.io) {
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
