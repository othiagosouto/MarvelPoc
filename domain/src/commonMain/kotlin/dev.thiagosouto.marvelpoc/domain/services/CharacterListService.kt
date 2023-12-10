package dev.thiagosouto.marvelpoc.domain.services

import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.service.ServiceList

/**
 * Interface to delivery [T]
 */
interface CharacterListService : ServiceList<CharacterListParams, Character>

data class CharacterListParams(
    val pageSize: Int,
    val queryText: String?
)
