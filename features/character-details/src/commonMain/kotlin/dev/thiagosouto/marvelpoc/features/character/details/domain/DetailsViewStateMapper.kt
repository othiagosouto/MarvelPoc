package dev.thiagosouto.marvelpoc.features.character.details.domain


import dev.thiagosouto.marvelpoc.domain.MapperList
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.features.character.details.DetailsViewState
import dev.thiagosouto.marvelpoc.support.presentation.PresentationMapper

/**
 * Mapper domain to DetailsViewState
 */
class DetailsViewStateMapper(
    private val comicsMapper: MapperList<Comics, dev.thiagosouto.marvelpoc.domain.model.Comics>
) : PresentationMapper<DetailsViewStateMapperInput, DetailsViewState> {
    override fun apply(
        input: DetailsViewStateMapperInput
    ): DetailsViewState {
        return DetailsViewState.Loaded(
            input.name,
            input.description,
            input.imageUrl,
            comics = comicsMapper.apply(input.comics)
        )
    }
}

data class DetailsViewStateMapperInput(
    val name: String,
    val description: String,
    val imageUrl: String,
    val comics: List<Comics>
)
