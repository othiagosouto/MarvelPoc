package com.soutosss.marvelpoc.home.list

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.home.HomeViewModel
import io.mockk.mockk
import org.hamcrest.Matchers.not
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.inject
import org.koin.dsl.module
import java.util.concurrent.Semaphore

fun configure(func: CharactersFragmentConfiguration.() -> Unit) =
    CharactersFragmentConfiguration().apply(func)

class CharactersFragmentConfiguration : KoinComponent {
    private lateinit var bundle: Bundle

    fun withFavoriteTab() {
        bundle = Bundle().apply { putBoolean("KEY_FAVORITE_TAB", true) }
    }

    fun withHomeTab() {
        bundle = Bundle().apply { putBoolean("KEY_FAVORITE_TAB", false) }
    }


    infix fun launch(func: CharactersFragmentRobot.() -> Unit): CharactersFragmentRobot {
        val mockRepository: CharactersRepository = mockk(relaxed = true)
        loadKoinModules(
            module(override = true) {
                single { mockRepository }
                single { HomeViewModel(get()) }
            })

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
