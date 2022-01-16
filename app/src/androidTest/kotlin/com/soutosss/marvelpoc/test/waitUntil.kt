package com.soutosss.marvelpoc.test

import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import junit.framework.AssertionFailedError
import org.hamcrest.Matchers.not
import java.util.concurrent.TimeoutException

fun ViewInteraction.waitUntilVisible(timeout: Long = 10_000L): ViewInteraction =
    waitUntil(matches(isDisplayed()), timeout)

fun ViewInteraction.waitUntilNotVisible(timeout: Long = 10_000L): ViewInteraction =
    waitUntil(matches(not(isDisplayed())), timeout)

private fun ViewInteraction.waitUntil(viewMatcher: ViewAssertion, timeout: Long): ViewInteraction {
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeout

    do {
        try {
            check(viewMatcher)
            return this
        } catch (e: AssertionFailedError) {
            Thread.sleep(50)
        }
    } while (System.currentTimeMillis() < endTime)

    throw TimeoutException()
}