package dev.thiagosouto.marvelpoc.base

import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule

abstract class BaseRobot(protected val rule: ComposeTestRule) {

    private fun ancestor(tag: String) = hasAnyAncestor(hasTestTag(tag))

    protected fun SemanticsNodeInteraction.waitUntilVisible() = apply {
        rule.waitUntil(1000L) { isValid { this@waitUntilVisible.assertIsDisplayed() } }
    }

    protected fun SemanticsNodeInteraction.waitUntilDoesNotExist() = apply {
        rule.waitUntil(1000L) { isValid { this@waitUntilDoesNotExist.assertDoesNotExist() } }
    }

    protected fun SemanticsNodeInteraction.waitUntilIsNotDisplayed() = apply {
        rule.waitUntil(1000L) { isValid { this@waitUntilIsNotDisplayed.assertIsNotDisplayed() } }
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
}
