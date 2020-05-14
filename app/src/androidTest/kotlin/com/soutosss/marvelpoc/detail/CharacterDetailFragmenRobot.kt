package com.soutosss.marvelpoc.detail

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.Character
import org.koin.core.KoinComponent

fun configureDetail(func: CharacterDetailsFragmentConfiguration.() -> Unit) =
    CharacterDetailsFragmentConfiguration().apply(func)

class CharacterDetailsFragmentConfiguration : KoinComponent {
    private lateinit var character: Character

    fun withEmptyDescriptionAndFavorite() {
        character = Character(30, "name", "thumbNail", "", true)
    }

    fun withSomeDescriptionNotFavorite() {
        character = Character(30, "name", "thumbNail", "Some description", false)
    }

    infix fun launch(func: CharacterDetailsFragmentRobot.() -> Unit): CharacterDetailsFragmentRobot {
        val bundle = Bundle().also { it.putSerializable("CHARACTER_KEY", character) }
        launchFragmentInContainer<CharacterDetailsFragment>(bundle)
        return CharacterDetailsFragmentRobot().apply(func)
    }

}

class CharacterDetailsFragmentRobot {
    infix fun check(func: CharacterDetailsFragmentResult.() -> Unit) =
        CharacterDetailsFragmentResult().apply(func)
}

class CharacterDetailsFragmentResult {

    fun checkCharacterName(){
        onView(withId(R.id.text)).check(matches(withText("name")))
    }
    fun checkExpectedCharacterDescription() {
        onView(withId(R.id.description)).check(matches(withText("Some description")))
    }

    fun checkEmptyCharacterDescription() {
        onView(withId(R.id.description)).check(matches(withText("This character don't have any description available :(")))
    }

    fun favoriteChecked() {
        onView(withId(R.id.favorite)).check(matches(isChecked()))
    }

    fun favoriteNotChecked() {
        onView(withId(R.id.favorite)).check(matches(isNotChecked()))
    }

}
