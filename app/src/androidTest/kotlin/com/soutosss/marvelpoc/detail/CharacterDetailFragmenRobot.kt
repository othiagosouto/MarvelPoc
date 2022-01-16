package com.soutosss.marvelpoc.detail

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.common.truth.Truth.assertThat
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.Character
import io.mockk.mockk
import io.mockk.verify
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun configureDetail(func: CharacterDetailsFragmentConfiguration.() -> Unit) =
    CharacterDetailsFragmentConfiguration().apply(func)

class CharacterDetailsFragmentConfiguration : KoinComponent {
    private lateinit var character: Character
    private val viewModel: CharacterDetailsViewModel = mockk(relaxed = true)

    fun withEmptyDescriptionAndFavorite() {
        character = Character(30, "name", "thumbNail", "", true)
    }

    fun withSomeDescriptionNotFavorite() {
        character = Character(30, "name", "thumbNail", "Some description", false)
    }

    infix fun launch(func: CharacterDetailsFragmentRobot.() -> Unit): CharacterDetailsFragmentRobot {

        loadKoinModules(
            module(override = true) {
                single { character }
                single { viewModel }
            })


        val bundle = Bundle().also { it.putSerializable("CHARACTER_KEY", character) }
        launchFragmentInContainer<CharacterDetailsFragment>(bundle)
        return CharacterDetailsFragmentRobot().apply(func)
    }

}

class CharacterDetailsFragmentRobot {
    infix fun check(func: CharacterDetailsFragmentResult.() -> Unit) =
        CharacterDetailsFragmentResult().apply(func)

    fun clickOnFavorite() {
        onView(withId(R.id.favorite)).perform(click())
    }
}

class CharacterDetailsFragmentResult : KoinComponent {
    private val character: Character by inject()
    private val viewModel: CharacterDetailsViewModel by inject()

    fun checkCharacterName() {
        onView(withId(R.id.text)).check(matches(withText("name")))
    }

    fun checkExpectedCharacterDescription() {
        onView(withId(R.id.description)).check(matches(withText("Some description")))
    }

    fun checkEmptyCharacterDescription() {
        onView(withId(R.id.description)).check(matches(withText("This character doesn't have any description available :(")))
    }

    fun favoriteChecked() {
        onView(withId(R.id.favorite)).check(matches(isChecked()))
    }

    fun favoriteNotChecked() {
        onView(withId(R.id.favorite)).check(matches(isNotChecked()))
    }

    fun checkFavoriteMethodFiredToFavorite() {
        assertThat(character.favorite).isTrue()
        verify { viewModel.favoriteClick(character) }
    }

    fun unFavorite() {
       onView(withId(R.id.favorite)).check(matches(isNotChecked()))
    }

}
