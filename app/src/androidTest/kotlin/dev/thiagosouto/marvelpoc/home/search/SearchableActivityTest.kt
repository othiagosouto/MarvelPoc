package dev.thiagosouto.marvelpoc.home.search

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class SearchableActivityTest {
    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    @Test
    fun whenActivityStartedWithSearchableIntent_shouldCallSearchApiWithExpectedContent() {
        configure(composeTestRule) {
            withSearchableIntent()
        } launch {

        } check {
            callViewModelWithExpectedContent()
        }
    }

    @Test
    fun  whenActivityStartedWithNotSearchableIntent_shouldNotCallSearchApi() {
        configure(composeTestRule) {
            withIntentNotSearchable()
        } launch {

        } check {
            notCallViewModel()
        }
    }
}
