package dev.thiagosouto.marvelpoc.data

import dev.thiagosouto.marvelpoc.domain.service.Service

/**
 * Interface to delivery [T]
 */
fun interface CharacterDetailsService : Service<String, CharacterDetails>
