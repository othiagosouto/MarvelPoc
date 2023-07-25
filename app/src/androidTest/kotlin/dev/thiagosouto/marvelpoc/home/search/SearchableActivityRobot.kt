package dev.thiagosouto.marvelpoc.home.search

import android.app.SearchManager
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.CharactersRepositoryImpl
import dev.thiagosouto.marvelpoc.home.list.CharactersViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

internal fun configure(func: SearchableActivityConfiguration.() -> Unit) =
    SearchableActivityConfiguration().apply(func)

internal class SearchableActivityConfiguration : KoinComponent {
    private val mockRepository: CharactersRepositoryImpl = mockk(relaxed = true)
    private val charactersViewModel: CharactersViewModel = spyk(CharactersViewModel(mockRepository, mockRepository))
    private lateinit var intent: Intent

    init {
        coEvery { mockRepository.fetchFavoriteIds() } returns emptyList()
        every { mockRepository.charactersPagingDataSource("ops", 20) } returns mockk(relaxed = true)
    }

    fun withSearchableIntent() {
        intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            SearchableActivity::class.java
        )
        intent.action = Intent.ACTION_SEARCH
        intent.putExtra(SearchManager.QUERY, "ops")
    }

    fun withIntentNotSearchable() {
        intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            SearchableActivity::class.java
        )
    }

    infix fun launch(func: SearchableActivityRobot.() -> Unit): SearchableActivityRobot {
        loadKoinModules(
            module(override = true) {
                single { mockRepository }
                single { charactersViewModel }
            })

        ActivityScenario.launch<SearchableActivity>(intent)
        return SearchableActivityRobot().apply(func)
    }
}

internal class SearchableActivityRobot {
    infix fun check(func: SearchableActivityResult.() -> Unit) =
        SearchableActivityResult().apply(func)
}

internal class SearchableActivityResult : KoinComponent {
    private val viewModel: CharactersViewModel by inject()

    fun callViewModelWithExpectedContent() {
        assertThat(viewModel.searchedQuery).isEqualTo("ops")
    }

    fun notCallViewModel() {
        assertThat(viewModel.searchedQuery).isNull()
    }
}
