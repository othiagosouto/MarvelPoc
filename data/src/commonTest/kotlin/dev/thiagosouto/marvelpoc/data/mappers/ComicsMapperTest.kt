package dev.thiagosouto.marvelpoc.data.mappers

import dev.thiagosouto.marvelpoc.data.Comics
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ComicsMapperTest {

    @Test
    fun `returns comics`() {
        val ids = listOf<Long>(1, 2, 3, 4)
        val comics = ids.map(::comicsDomain)

        val mapper = ComicsMapper()

        val expectedResult = ids.map(::comicsView)
        assertEquals(expected = expectedResult, actual = mapper.apply(comics))
    }

    private fun comicsDomain(id: Long) =
        Comics(id = id, title = "title - $id", imageUrl = "thumb-$id")

    private fun comicsView(id: Long) = dev.thiagosouto.marvelpoc.domain.model.Comics(
        title = "title - $id",
        thumbnailUrl = "thumb-$id"
    )
}
