package dev.thiagosouto.marvelpoc.shared.livedata

sealed class Result {
    data class Error(val errorMessage: Int, val drawableRes: Int) : Result()
    object Loaded : Result()
}
