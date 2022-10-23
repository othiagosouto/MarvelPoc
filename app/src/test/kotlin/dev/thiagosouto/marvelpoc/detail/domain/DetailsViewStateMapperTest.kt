package dev.thiagosouto.marvelpoc.detail.domain

import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.detail.DetailsViewState
import org.junit.Test

class DetailsViewStateMapperTest {

    @Test
    fun `returns DetailsViewState`() {
        val mapper = DetailsViewStateMapper(ComicsMapper())
        val input =
            DetailsViewStateMapper.Input(
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
                dev.thiagosouto.marvelpoc.data.model.view.Comics(
                    title = input.comics.first().title,
                    thumbnailUrl = input.comics.first().imageUrl
                )
            )
        )
        assertThat(mapper.apply(input)).isEqualTo(expected)
    }
}
