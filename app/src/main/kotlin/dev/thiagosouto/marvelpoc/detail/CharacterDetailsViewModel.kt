package dev.thiagosouto.marvelpoc.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thiagosouto.marvelpoc.data.CharactersRepository
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.shared.mvi.MviView
import dev.thiagosouto.marvelpoc.shared.mvi.Presenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val repository: CharactersRepository,
    private val comicsMapper: ComicsMapper
) : ViewModel(),
    Presenter<Intent, DetailsViewState> {

    private lateinit var view: MviView<DetailsViewState>
    override fun process(intent: Intent) {
        processIntent(intent)
    }

    private fun processIntent(intent: Intent) {
        when (intent) {
            is Intent.OpenScreen -> {
                process(Intent.Internal.LoadScreen(intent.characterId))
            }
            is Intent.Internal.LoadScreen -> {
                viewModelScope.launch(Dispatchers.IO) {
                    view.render(DetailsViewState.Loading)
                    val details = repository.fetchCharacterDetails(intent.characterId.toString())
                    view.render(
                        DetailsViewState.Loaded(
                            details.name,
                            details.description,
                            details.imageUrl,
                            comics = comicsMapper.apply(details.comics)
                        )
                    )
                }
            }
            is Intent.CloseScreen -> view.render(DetailsViewState.Closed)
        }
    }

    override fun bind(view: MviView<DetailsViewState>) {
        this.view = view
        this.view.render(DetailsViewState.Idle)
    }
}
