package com.soutosss.marvelpoc.home.list

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.home.HomeViewModel
import com.soutosss.marvelpoc.shared.livedata.Result
import io.mockk.mockk
import org.hamcrest.Matchers.not
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun configure(func: CharactersFragmentConfiguration.() -> Unit) =
    CharactersFragmentConfiguration().apply(func)

class CharactersFragmentConfiguration : KoinComponent {
    private lateinit var bundle: Bundle
    private val mockRepository: CharactersRepository = mockk(relaxed = true)
    private val homeViewModel: HomeViewModel = HomeViewModel(mockRepository)

    fun withFavoriteTab() {
        bundle = Bundle().apply { putBoolean("KEY_FAVORITE_TAB", true) }
    }

    fun withHomeTab() {
        bundle = Bundle().apply { putBoolean("KEY_FAVORITE_TAB", false) }
    }

    infix fun launch(func: CharactersFragmentRobot.() -> Unit): CharactersFragmentRobot {
        loadKoinModules(
            module(override = true) {
                single { mockRepository }
                single { homeViewModel }
            })

        launchFragmentInContainer<CharactersFragment>(bundle)
        return CharactersFragmentRobot().apply(func)
    }

    fun withEmptyFavoriteResult() {
        postLiveData(homeViewModel.favoriteCharacters, Result.Loaded(emptyList<CharacterHome>()))
    }

    fun withErrorFavorite() {
        postLiveData(homeViewModel.favoriteCharacters, Result.Error(R.string.favorite_error_loading, R.drawable.thanos))
    }

    fun withErrorHome() {
        postLiveData(homeViewModel.characters, Result.Error(R.string.home_error_loading, R.drawable.thanos))
    }

    fun withEmptyHomeResult() {
        postLiveData(homeViewModel.characters, Result.Loaded(emptyList<CharacterHome>()))
    }

    fun withHomeCharacters() {
        val item = CharacterHome(
            30,
            "3-D Man",
            "http://www.google.com",
            false
        )

        postLiveData(homeViewModel.characters, Result.Loaded(listOf(item)))
    }

    fun withFavoriteCharacters() {
        val item = CharacterHome(
            30,
            "3-D Test HAHAH",
            "http://www.google.com",
            false
        )
        postLiveData(homeViewModel.favoriteCharacters, Result.Loaded(listOf(item)))
    }

    private fun postLiveData(liveData: LiveData<Result>, item: Result) {
        val mutableLiveData: MutableLiveData<Result> = liveData as MutableLiveData<Result>
        mutableLiveData.postValue(item)
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

    fun checkCharacterHomeNamee() {
        checkCharacterName("3-D Man")
    }

    fun characterFavoriteName() {
        checkCharacterName("3-D Test HAHAH")
    }

    private fun checkCharacterName(characterName: String) {
        onView(withId(R.id.recycler)).check(matches(hasDescendant(withText(characterName))))
    }

    fun loadingIsVisible() {
        onView(withId(R.id.progress)).check(matches(isDisplayed()))
    }

    fun loadingIsNotVisible() {
        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
    }

    fun checkEmptyFavoriteTab() {
        checkErrorMessage("You don't have favorite marvel character :(")
    }

    fun checkErrorFavoriteTab() {
        checkErrorMessage("Looks like thanos didn't like your favorite characters")
    }

    fun checkErrorHomeTab() {
        checkErrorMessage("Looks like thanos didn't like you")
    }

    fun checkEmptyHomeTab() {
        checkErrorMessage("There`s no characters available :(")
    }

    fun errorMessageNotAvailable() {
        onView(withId(R.id.message)).check(matches(not(isDisplayed())))
        onView(withId(R.id.erroIcon)).check(matches(not(isDisplayed())))
    }

    private fun checkErrorMessage(message: String) {
        onView(withId(R.id.message)).check(matches(isDisplayed()))
        onView(withId(R.id.erroIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.message)).check(matches(withText(message)))
    }

}
