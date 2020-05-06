package com.soutosss.marvelpoc.shared.livedata

sealed class Result {
    object Loading : Result()
    data class Error(val errorMessage: Int) : Result()
    data class Loaded(val item: Any) : Result()
    data class LoadedList(val item: List<*>) : Result()
}