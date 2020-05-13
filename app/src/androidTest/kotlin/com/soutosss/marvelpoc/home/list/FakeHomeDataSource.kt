package com.soutosss.marvelpoc.home.list

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.soutosss.marvelpoc.data.model.view.CharacterHome

class FakeHomeDataSource(private val items: List<CharacterHome>) :
    DataSource.Factory<Int, CharacterHome>() {
    override fun create(): DataSource<Int, CharacterHome> = FakeDataSource(items)

    class FakeDataSource(var items: List<CharacterHome>) : PositionalDataSource<CharacterHome>() {
        override fun loadRange(
            params: LoadRangeParams,
            callback: LoadRangeCallback<CharacterHome>
        ) {
            callback.onResult(emptyList())
        }

        override fun loadInitial(
            params: LoadInitialParams,
            callback: LoadInitialCallback<CharacterHome>
        ) {
            val totalCount = items.size

            val position = computeInitialLoadPosition(params, totalCount)
            val loadSize = computeInitialLoadSize(params, position, totalCount)

            val sublist = items.subList(position, position + loadSize)
            callback.onResult(sublist, position, totalCount)
        }

    }
}
