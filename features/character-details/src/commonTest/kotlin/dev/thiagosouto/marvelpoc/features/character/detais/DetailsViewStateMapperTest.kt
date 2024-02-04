package dev.thiagosouto.marvelpoc.features.character.detais

import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.features.character.details.DetailsViewState
import dev.thiagosouto.marvelpoc.features.character.details.domain.DetailsViewStateMapper
import dev.thiagosouto.marvelpoc.features.character.details.domain.DetailsViewStateMapperInput
import dev.thiagosouto.marvelpoc.support.presentation.PresentationMapper
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DetailsViewStateMapperTest {

    @Test
    fun `returns DetailsViewState`() {
        val mapper: PresentationMapper<DetailsViewStateMapperInput, DetailsViewState> =
            DetailsViewStateMapper(ComicsMapper())
        val input =
            DetailsViewStateMapperInput(
                name = "some-name",
                description = "some-description",
                imageUrl = "image-url",
                comics = listOf(
                    Comics(
                        id = 3,
                        title = "some-title",
                        imageUrl = "some-comics-image-url"
                    )
                )
            )

        val expected = DetailsViewState.Loaded(
            name = input.name,
            description = input.description,
            imageUrl = input.imageUrl,
            comics = listOf(
                dev.thiagosouto.marvelpoc.domain.model.Comics(
                    title = input.comics.first().title,
                    thumbnailUrl = input.comics.first().imageUrl
                )
            )
        )
        assertEquals(expected, actual = mapper.apply(input))
    }
}
