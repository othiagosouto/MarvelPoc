package com.soutosss.marvelpoc.home.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class SearchableActivityTest {

    @Test
    fun whenActivityStartedWithSearchableIntent_shouldCallSearchApiWithExpectedContent() {
        configure {
            withSearchableIntent()
        } launch {

        } check {
            callViewModelWithExpectedContent()
        }
    }

    @Test
    fun  whenActivityStartedWithNotSearchableIntent_shouldNotCallSearchApi() {
        configure {
            withIntentNotSearchable()
        } launch {

        } check {
            notCallViewModel()
        }
    }

}