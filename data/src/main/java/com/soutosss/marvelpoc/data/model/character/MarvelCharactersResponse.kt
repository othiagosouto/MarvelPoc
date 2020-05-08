package com.soutosss.marvelpoc.data.model.character

import com.soutosss.marvelpoc.data.model.view.CharacterHome

data class MarvelCharactersResponse(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    val data: Data,
    val etag: String,
    val status: String
) 


fun MarvelCharactersResponse.toCharacterHomeList(): List<CharacterHome> =
    this.data.results.map { CharacterHome(it) }