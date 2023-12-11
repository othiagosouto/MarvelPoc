package dev.thiagosouto.marvelpoc.domain.services

import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.service.ServiceList

/**
 * Interface to delivery List of Characters
 */
interface CharacterListService : ServiceList<CharacterListParams, Character>

/**
 * Input used for [CharacterListService]
 */
data class CharacterListParams(
    val pageSize: Int,
    val queryText: String?
)
