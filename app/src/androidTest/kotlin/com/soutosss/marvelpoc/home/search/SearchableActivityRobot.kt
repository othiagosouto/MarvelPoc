package com.soutosss.marvelpoc.home.search

import android.app.SearchManager
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.home.HomeViewModel
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

internal fun configure(func: SearchableActivityConfiguration.() -> Unit) =
    SearchableActivityConfiguration().apply(func)

internal class SearchableActivityConfiguration : KoinComponent {
    private val mockRepository: CharactersRepository = mockk(relaxed = true)
    private val homeViewModel: HomeViewModel = spyk(HomeViewModel(mockRepository))
    private lateinit var intent: Intent

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
                single { homeViewModel }
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
    private val viewModel: HomeViewModel by inject()

    fun callViewModelWithExpectedContent() {
        verify { viewModel.searchedQuery = "ops" }
    }

    fun notCallViewModel() {
        assertThat(viewModel.searchedQuery).isNull()
    }
}
