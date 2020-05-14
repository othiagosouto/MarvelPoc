package com.soutosss.marvelpoc.home

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.soutosss.marvelpoc.data.model.view.Character

class FakeHomeDataSource(private val items: List<Character>) :
    DataSource.Factory<Int, Character>() {
    override fun create(): DataSource<Int, Character> = FakeDataSource(items)

    class FakeDataSource(var items: List<Character>) : PositionalDataSource<Character>() {
        override fun loadRange(
            params: LoadRangeParams,
            callback: LoadRangeCallback<Character>
        ) {
            callback.onResult(
                items.subList(
                    params.startPosition,
                    params.startPosition + params.loadSize
                )
            )
        }

        override fun loadInitial(
            params: LoadInitialParams,
            callback: LoadInitialCallback<Character>
        ) {
            val totalCount = items.size

            val position = computeInitialLoadPosition(params, totalCount)
            val loadSize = computeInitialLoadSize(params, position, totalCount)

            val sublist = items.subList(position, position + loadSize)
            callback.onResult(sublist, position, totalCount)
        }

    }
}