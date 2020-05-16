package com.soutosss.data.data_retrofit

import androidx.paging.PositionalDataSource
import com.soutosss.data.data_retrofit.ext.toCharacter
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.shared.EmptyDataException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CharactersDataSource(
    private val queryText: String?,
    private val scope: CoroutineScope,
    private val api: CharactersApi,
    private val exceptionHandler: (Exception) -> Unit,
    private val loadFinished: () -> Unit
) :
    PositionalDataSource<Character>() {

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<Character>
    ) {
        scope.launch {
            try {
                val response = api.listCharacters(
                    name = queryText,
                    offset = 0,
                    limit = params.requestedLoadSize
                ).data.results.map { it.toCharacter() }
                if (response.isEmpty()) {
                    throw EmptyDataException()
                }
                callback.onResult(response, 0)
                loadFinished()
            } catch (e: Exception) {
                exceptionHandler(e)
            }
        }
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<Character>
    ) {
        scope.launch {
            try {
                val response =
                    api.listCharacters(
                        name = queryText,
                        offset = params.startPosition,
                        limit = params.loadSize
                    ).data.results.map { it.toCharacter() }
                callback.onResult(response)
            } catch (e: Exception) {
                exceptionHandler(e)
            }
        }
    }

}
