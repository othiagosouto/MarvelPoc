package com.soutosss.marvelpoc.data.mappers

import com.soutosss.marvelpoc.data.Comics
import org.junit.Assert.assertEquals
import org.junit.Test

class ComicsMapperTest {

    @Test
    fun `returns comics`() {
        val ids = listOf<Long>(1, 2, 3, 4)
        val comics = ids.map(::comicsDomain)

        val mapper = ComicsMapper()

        val expectedResult = ids.map(::comicsView)
        assertEquals(expectedResult, mapper.apply(comics))
    }

    private fun comicsDomain(id: Long) =
        Comics(id = id, title = "title - $id", imageUrl = "thumb-$id")

    private fun comicsView(id: Long) = com.soutosss.marvelpoc.data.model.view.Comics(
        title = "title - $id",
        thumbnailUrl = "thumb-$id"
    )
}
