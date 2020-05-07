package com.soutosss.marvelpoc.home.list

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.soutosss.marvelpoc.R
import org.hamcrest.Matchers.not

fun configure(func: CharactersFragmentConfiguration.() -> Unit) =
    CharactersFragmentConfiguration().apply(func)


class CharactersFragmentConfiguration {
    private lateinit var bundle: Bundle

    fun withFavoriteTab() {
        bundle = Bundle().apply { putBoolean("KEY_FAVORITE_TAB", true) }
    }

    fun withHomeTab() {
        bundle = Bundle().apply { putBoolean("KEY_FAVORITE_TAB", false) }
    }

    infix fun launch(func: CharactersFragmentRobot.() -> Unit): CharactersFragmentRobot {
        launchFragmentInContainer<CharactersFragment>(bundle)

        return CharactersFragmentRobot().apply(func)
    }
}

class CharactersFragmentRobot {

    infix fun check(func: CharactersFragmentResult.() -> Unit) =
        CharactersFragmentResult().apply(func)

}

class CharactersFragmentResult {
    fun recyclerViewIsHidden() {
        onView(withId(R.id.recycler)).check(matches(not(isDisplayed())))
    }

    fun loadingIsVisible() {
        onView(withId(R.id.progress)).check(matches(isDisplayed()))
    }

}
