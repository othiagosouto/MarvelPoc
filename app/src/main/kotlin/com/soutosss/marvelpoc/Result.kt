package com.soutosss.marvelpoc

sealed class Result {
    object Loading : Result()
    data class Error(val errorMessage: Int) : Result()
    data class Loaded(val users: Any) : Result()
}
