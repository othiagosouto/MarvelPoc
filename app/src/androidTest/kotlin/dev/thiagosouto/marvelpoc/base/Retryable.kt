package dev.thiagosouto.marvelpoc.base

import androidx.compose.ui.test.junit4.ComposeTestRule

internal interface Retryable {

    fun retry(count: Int = 0, max: Int = 5, func: ComposeTestRule.() -> Unit)

    fun retryWithDelay(
        count: Int = 0,
        max: Int = 5,
        delay: Long = 50L,
        errorHandling: ComposeTestRule.() -> Unit = { },
        func: ComposeTestRule.() -> Unit
    )
}
