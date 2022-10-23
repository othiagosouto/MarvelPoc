package dev.thiagosouto.marvelpoc.base

import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag

internal abstract class BaseRobot(protected val rule: ComposeTestRule) :
    Retryable by RetryRobot(rule) {

    private fun ancestor(tag: String) = hasAnyAncestor(hasTestTag(tag))

    protected fun SemanticsNodeInteraction.waitUntilVisible() = apply {
        rule.waitUntil(LONG_TIMEOUT) { isValid { this.assertIsDisplayed() } }
    }

    protected fun SemanticsNodeInteraction.waitUntilDoesNotExist() = apply {
        rule.waitUntil(LONG_TIMEOUT) { isValid { this.assertDoesNotExist() } }
    }

    protected fun SemanticsNodeInteraction.waitUntilIsNotDisplayed() = apply {
        rule.waitUntil(LONG_TIMEOUT) { isValid { this.assertIsNotDisplayed() } }
    }

    private fun isValid(passedFunction: () -> Unit): Boolean =
        try {
            passedFunction()
            true
        } catch (ex1: ComposeTimeoutException) {
            false
        } catch (e: java.lang.AssertionError) {
            false
        }

    fun applyComposable(func: ComposeTestRule.() -> Unit) = apply {
        rule.func()
    }

    fun assertTagDoesNotExist(tag: String) {
        rule
            .onNodeWithTag(tag)
            .assertDoesNotExist()
    }

    private companion object {
        const val LONG_TIMEOUT = 1000L
    }
}
