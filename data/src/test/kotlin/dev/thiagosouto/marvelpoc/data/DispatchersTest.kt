package dev.thiagosouto.marvelpoc.data

import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Test

class DispatchersTest {

    @Test
    fun `returns specific dispatchers from coroutines`() {
        val dispatchers = Dispatchers()

        assertThat(dispatchers.main).isEqualTo(kotlinx.coroutines.Dispatchers.Main)
        assertThat(dispatchers.io).isEqualTo(kotlinx.coroutines.Dispatchers.IO)
        assertThat(dispatchers.default).isEqualTo(kotlinx.coroutines.Dispatchers.Default)
    }
}
