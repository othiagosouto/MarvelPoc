package dev.thiagosouto.marvelpoc.data

import kotlinx.coroutines.IO
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DispatchersTest {

    @Test
    fun `returns specific dispatchers from coroutines`() {
        val dispatchers = Dispatchers()

        assertEquals(expected = kotlinx.coroutines.Dispatchers.Main, actual = dispatchers.main)
        assertEquals(expected = kotlinx.coroutines.Dispatchers.IO, actual = dispatchers.io)
        assertEquals(
            expected = kotlinx.coroutines.Dispatchers.Default,
            actual = dispatchers.default
        )
    }
}
