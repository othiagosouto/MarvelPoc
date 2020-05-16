package com.soutosss.marvelpoc.home

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.soutosss.marvelpoc.data.room_source.CharacterLocal

class FakeHomeDataSource(private val items: List<CharacterLocal>) :
    DataSource.Factory<Int, CharacterLocal>() {
    override fun create(): DataSource<Int, CharacterLocal> = FakeDataSource(items)

    class FakeDataSource(var items: List<CharacterLocal>) : PositionalDataSource<CharacterLocal>() {
        override fun loadRange(
            params: LoadRangeParams,
            callback: LoadRangeCallback<CharacterLocal>
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
            callback: LoadInitialCallback<CharacterLocal>
        ) {
            val totalCount = items.size

            val position = computeInitialLoadPosition(params, totalCount)
            val loadSize = computeInitialLoadSize(params, position, totalCount)

            val sublist = items.subList(position, position + loadSize)
            callback.onResult(sublist, position, totalCount)
        }

    }
}
