package com.soutosss.marvelpoc.test

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import junit.framework.AssertionFailedError
import org.hamcrest.Matchers.not
import java.util.concurrent.TimeoutException

fun ViewInteraction.waitUntilVisible(timeout: Long): ViewInteraction {
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeout

    do {
        try {
            check(matches(isDisplayed()))
            return this
        } catch (e: AssertionFailedError) {
            Thread.sleep(50)
        }
    } while (System.currentTimeMillis() < endTime)

    throw TimeoutException()
}

fun ViewInteraction.waitUntilNotVisible(timeout: Long): ViewInteraction {
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeout

    do {
        try {
            check(matches(not(isDisplayed())))
            return this
        } catch (e: AssertionFailedError) {
            Thread.sleep(50)
        }
    } while (System.currentTimeMillis() < endTime)

    throw TimeoutException()
}