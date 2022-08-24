package dev.thiagosouto.marvelpoc.base

import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.junit4.ComposeTestRule

internal class RetryRobot(private val rule: ComposeTestRule) : Retryable {
    override fun retry(count: Int, max: Int, func: ComposeTestRule.() -> Unit) {
        try {
            rule.func()
        } catch (e: ComposeTimeoutException) {
            retryException(e, count, max, func)
        } catch (e: AssertionError) {
            retryException(e, count, max, func)
        }
    }

    private fun retryException(
        e: Throwable,
        count: Int = 0,
        max: Int = 5,
        func: ComposeTestRule.() -> Unit
    ) {
        if (count == max) {
            throw e
        }
        retry(count + 1, max, func)
    }

    override fun retryWithDelay(
        count: Int,
        max: Int,
        delay: Long,
        func: ComposeTestRule.() -> Unit
    ) {
        try {
            rule.func()
        } catch (e: ComposeTimeoutException) {
            retryException(delay, e, count + 1, max, func)
        } catch (e: AssertionError) {
            retryException(delay, e, count + 1, max, func)
        }
    }

    private fun retryException(
        delay: Long,
        e: Throwable,
        count: Int = 0,
        max: Int = 5,
        func: ComposeTestRule.() -> Unit
    ) {
        if (count == max) {
            throw e
        }

        if (delay != 0L) {
            Thread.sleep(delay)
        }
        retryWithDelay(count + 1, max, delay, func)
    }
}
