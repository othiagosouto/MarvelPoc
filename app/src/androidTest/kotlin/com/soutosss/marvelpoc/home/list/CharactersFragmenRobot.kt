package com.soutosss.marvelpoc.home.list

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.gson.Gson
import com.soutosss.data.data_retrofit.CharactersApi
import com.soutosss.data.data_retrofit.character.MarvelCharactersResponse
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.character.CharacterLocalContract
import com.soutosss.marvelpoc.home.HomeViewModel
import com.soutosss.marvelpoc.shared.livedata.Result
import io.mockk.coEvery
import io.mockk.mockk
import org.hamcrest.Matchers.not
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import com.soutosss.marvelpoc.data.room_source.CharacterLocal
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

fun configure(func: CharactersFragmentConfiguration.() -> Unit) =
    CharactersFragmentConfiguration().apply(func)

class CharactersFragmentConfiguration : KoinComponent {
    private val api: CharactersApi = mockk(relaxed = true)
    private val mockDao: CharacterLocalContract<CharacterLocal> = mockk(relaxed = true)
    private val repository: CharactersRepository
    private var homeViewModel: HomeViewModel

    init {
        loadKoinModules(
            module(override = true) {
                single { api }
                single { mockDao }
            })
        repository = CharactersRepository(get(), get())
        homeViewModel = HomeViewModel(repository)
    }

    infix fun launch(func: CharactersFragmentRobot.() -> Unit): CharactersFragmentRobot {
        loadKoinModules(
            module(override = true) {
                single { mockDao }
                single { api }
                single { repository }
                single { homeViewModel }
            })

        launchFragmentInContainer<CharactersFragment>()
        return CharactersFragmentRobot().apply(func)
    }

    infix fun launchSearch(func: CharactersFragmentRobot.() -> Unit): CharactersFragmentRobot {
        loadKoinModules(
            module(override = true) {
                single { mockDao }
                single { api }
                single { repository }
                single { homeViewModel }
            })

        launchFragmentInContainer<CharactersFragment>(Bundle().apply {
            putString(
                "QUERY_TEXT_KEY",
                "searchQuery"
            )
        })
        return CharactersFragmentRobot().apply(func)
    }

    fun withErrorHome() {
        postLiveData(
            homeViewModel.characters,
            Result.Error(R.string.home_error_loading, R.drawable.thanos)
        )
    }

    fun withHomeCharacters() {
        coEvery { api.listCharacters(null, any(), any()) } returns parseToJson()
    }

    fun withSearchContent() {
        coEvery { api.listCharacters("searchQuery", any(), any()) } returns parseToJson()
    }

    fun withNoFavorites() {
        coEvery { mockDao.favoriteIds() } returns emptyList()
    }

    private fun postLiveData(liveData: LiveData<Result>, item: Result) {
        val mutableLiveData: MutableLiveData<Result> = liveData as MutableLiveData<Result>
        mutableLiveData.postValue(item)
    }

    fun withMockedViewModelLoading() {
        homeViewModel = mockk(relaxed = true)
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

    fun checkCharacterName() {
        checkCharacterName("3-D Man")
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

    fun checkErrorHomeTab() {
        checkErrorMessage("Looks like thanos didn't like you")
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

private fun parseToJson(): MarvelCharactersResponse {
    return Gson().fromJson(
        "characters/characters_response_ok.json".toJson(),
        MarvelCharactersResponse::class.java
    )
}

private fun String.toJson(): String {
    return CharactersFragmentConfiguration::class.java.classLoader!!.getResource(this)!!.readText()
}
