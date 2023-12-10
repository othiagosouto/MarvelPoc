package dev.thiagosouto.marvelpoc.data.mappers

import dev.thiagosouto.marvelpoc.domain.MapperList
import dev.thiagosouto.marvelpoc.data.Comics

/**
 * Mapper to comics domain to ui model
 */
class ComicsMapper: MapperList<Comics, dev.thiagosouto.marvelpoc.domain.model.Comics> {

    /**
     * Mapper to comics domain to ui model
     */
    override fun apply(input: List<Comics>): List<dev.thiagosouto.marvelpoc.domain.model.Comics> =
        input.map {
            dev.thiagosouto.marvelpoc.domain.model.Comics(
                title = it.title,
                thumbnailUrl = it.imageUrl
            )
        }
}
