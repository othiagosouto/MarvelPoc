package dev.thiagosouto.marvelpoc.detail.domain

import dev.thiagosouto.domain.Mapper
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.detail.DetailsViewState

/**
 * Mapper domain to DetailsViewState
 */
internal class DetailsViewStateMapper(
    private val comicsMapper: ComicsMapper
): Mapper<DetailsViewStateMapper.Input, DetailsViewState> {
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
