package com.soutosss.marvelpoc.data.character

import androidx.paging.PositionalDataSource
import kotlinx.coroutines.CoroutineScope

interface CharacterRemoteContract<T> {
    fun listCharacters(
        scope: CoroutineScope,
        queryText: String? = null,
        exceptionHandler: (Exception) -> Unit,
        loadFinished: () -> Unit
    ): PositionalDataSource<T>
}