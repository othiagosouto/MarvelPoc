package com.soutosss.marvelpoc.home.search

import org.junit.Test

class SearchableActivityTest {


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