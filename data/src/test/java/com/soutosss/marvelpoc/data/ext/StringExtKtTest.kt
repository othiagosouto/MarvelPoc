package com.soutosss.marvelpoc.data.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringExtKtTest {

    @Test
    fun toMD5_shouldCreateAValidMD5Hash() {
        assertThat("1323123d".toMD5()).isEqualTo("e2526bbcc3c041bd6d39f59bb57b4940")
    }
}