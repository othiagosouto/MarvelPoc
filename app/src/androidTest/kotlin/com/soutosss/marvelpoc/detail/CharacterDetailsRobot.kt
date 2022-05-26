package com.soutosss.marvelpoc.detail

import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharacterDetails
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.Comics
import com.soutosss.marvelpoc.data.mappers.ComicsMapper
import com.soutosss.marvelpoc.data.model.view.Character
import io.mockk.coEvery
import io.mockk.mockk
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

internal fun configureDetail(func: CharacterDetailsConfiguration.() -> Unit) =
    CharacterDetailsConfiguration().apply(func)

internal class CharacterDetailsConfiguration() : KoinComponent {
    private lateinit var character: Character
    private val repository: CharactersRepository = mockk(relaxed = true)
    private val viewModel: CharacterDetailsViewModel =
        CharacterDetailsViewModel(repository, ComicsMapper())
    private lateinit var rule: ComposeTestRule

    fun withComposeTestRule(rule: ComposeTestRule) {
        this.rule = rule
    }

    fun withEmptyDescription() {
        character = Character(30, "some name", "thumbNail", "", false)
        coEvery { repository.fetchCharacterDetails("30") } returns CharacterDetails(
            character.id,
            character.name,
            character.description,
            character.thumbnailUrl,
            emptyList()
        )
    }

    fun withSomeDescription() {
        character = Character(30, "some name", "thumbNail", "Some description", false)
        coEvery { repository.fetchCharacterDetails("30") } returns CharacterDetails(
            character.id,
            character.name,
            character.description,
            character.thumbnailUrl,
            ids().map(::comicsDomain)
        )
    }

    infix fun launch(func: CharacterDetailsRobot.() -> Unit): CharacterDetailsRobot {
        loadKoinModules(
            module(override = true) {
                single { viewModel }
            })

        val bundle = Bundle().also { it.putSerializable("CHARACTER_KEY", character) }

        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CharacterDetailsActivity::class.java
        )
        intent.putExtra("CHARACTER_KEY", character)
        ActivityScenario.launch<CharacterDetailsActivity>(intent)
        return CharacterDetailsRobot(rule).apply(func)
    }

}

internal class CharacterDetailsRobot(private val rule: ComposeTestRule) {
    infix fun check(func: CharacterDetailsResult.() -> Unit) =
        CharacterDetailsResult(rule).apply(func)
}

internal class CharacterDetailsResult(private val rule: ComposeTestRule) {

    fun characterName() {
        rule.onNodeWithTag("name").assert(hasText("some name"))
    }

    fun description() {
        rule.onNodeWithTag("description").assert(hasText("Some description"))
    }

    fun defaultDescription() {
        rule.onNodeWithTag("description")
            .assert(hasText("This character doesn't have any description available :("))
    }

    fun comics() {
        ids().map(::titles).forEachIndexed(::comics)
    }

    private fun comics(index: Int, title: String) {
        val item = rule.onNodeWithTag("character-detais-comics").performScrollToIndex(index)
        rule.onNodeWithTag("comics-title-$index").assert(hasText(title)).assertIsDisplayed()
    }
}

private fun ids() = listOf<Long>(0, 1, 3, 4, 5)
private fun comicsDomain(id: Long) =
    Comics(id = id, title = "title - $id", imageUrl = "thumb-$id")

private fun titles(id: Long) = "title - $id"
