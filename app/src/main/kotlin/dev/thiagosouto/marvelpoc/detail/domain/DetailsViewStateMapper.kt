package dev.thiagosouto.marvelpoc.detail.domain

import dev.thiagosouto.domain.MapperList
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.detail.DetailsViewState
import dev.thiagosouto.marvelpoc.support.presentation.PresentationMapper

/**
 * Mapper domain to DetailsViewState
 */
internal class DetailsViewStateMapper(
    private val comicsMapper: MapperList<Comics, dev.thiagosouto.domain.model.Comics>
) : PresentationMapper<DetailsViewStateMapper.Input, DetailsViewState> {
    override fun apply(
        input: Input
    ): DetailsViewState {
        return DetailsViewState.Loaded(
            input.name,
            input.description,
            input.imageUrl,
            comics = comicsMapper.apply(input.comics)
        )
    }

    data class Input(
        val name: String,
        val description: String,
        val imageUrl: String,
        val comics: List<Comics>
    )
}
