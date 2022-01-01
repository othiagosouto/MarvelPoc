package com.soutosss.marvelpoc.data.character

import androidx.paging.PositionalDataSource
import kotlinx.coroutines.CoroutineScope

interface CharacterRemoteContract<T : Any> {
    fun listCharacters(
        scope: CoroutineScope,
        queryText: String? = null,
        exceptionHandler: (Exception) -> Unit,
        loadFinished: () -> Unit,
        provideFavoriteIds: suspend () -> List<Long>
    ): PositionalDataSource<T>
}