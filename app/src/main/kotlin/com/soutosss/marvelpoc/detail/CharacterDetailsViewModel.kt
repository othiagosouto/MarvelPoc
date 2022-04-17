package com.soutosss.marvelpoc.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.mappers.ComicsMapper
import com.soutosss.marvelpoc.data.model.view.Comics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val repository: CharactersRepository,
    private val comicsMapper: ComicsMapper
) : ViewModel(),
    Presenter<Intent, DetailsViewState, Effect> {

    private val _state: MutableStateFlow<DetailsViewState> = MutableStateFlow(DetailsViewState.Idle)
    override val state: StateFlow<DetailsViewState> = _state

    private val _effects: MutableStateFlow<Effect> = MutableStateFlow(Effect.Idle)
    override val effects: StateFlow<Effect> = _effects

    override fun process(intent: Intent) {
        processIntent(intent)?.let {
            processToEffect(intent)
        }
    }

    private fun processToEffect(intent: Intent) {
        if (intent is Intent.CloseScreen) _effects.value = Effect.CloseScreen
    }

    private fun processIntent(intent: Intent): Intent? = when (intent) {
        is Intent.OpenScreen -> {
            process(Intent.Internal.LoadScreen(intent.characterId))
            null
        }
        is Intent.Internal.LoadScreen -> {
            viewModelScope.launch(Dispatchers.IO) {
                _state.value = DetailsViewState.Loading
                val details = repository.fetchCharacterDetails(intent.characterId.toString())
                _state.value = DetailsViewState.Loaded(
                    details.name,
                    details.description,
                    details.imageUrl,
                    comics = comicsMapper.apply(details.comics)
                )
            }
            null
        }
        else -> intent
    }
}
