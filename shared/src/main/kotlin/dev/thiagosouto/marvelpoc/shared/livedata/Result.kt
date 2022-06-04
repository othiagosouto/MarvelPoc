package dev.thiagosouto.marvelpoc.shared.livedata

sealed class Result {

    /**
     * Result to represent an error
     * @param errorMessage is the message id from string resources
     * @param drawableRes is the drawable id from resources
     */
    data class Error(val errorMessage: Int, val drawableRes: Int) : Result()

    /**
     * Result to represent state of loaded
     */
    object Loaded : Result()
}