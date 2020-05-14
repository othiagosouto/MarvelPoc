package com.soutosss.marvelpoc

import com.google.gson.Gson
import com.soutosss.marvelpoc.data.network.character.MarvelCharactersResponse

fun parseToJson(): MarvelCharactersResponse {
    return Gson().fromJson(
        "/characters/characters_response_ok.json".toJson(),
        MarvelCharactersResponse::class.java
    )
}

private fun String.toJson(): String {
    return this::class.java.javaClass.getResource(this)!!.readText()
}