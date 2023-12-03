package dev.thiagosouto.marvelpoc.data.mappers

import dev.thiagosouto.domain.MapperList
import dev.thiagosouto.marvelpoc.data.Comics

/**
 * Mapper to comics domain to ui model
 */
class ComicsMapper: MapperList<Comics, dev.thiagosouto.domain.model.Comics> {

    /**
     * Mapper to comics domain to ui model
     */
    override fun apply(input: List<Comics>): List<dev.thiagosouto.domain.model.Comics> =
        input.map {
            dev.thiagosouto.domain.model.Comics(
                title = it.title,
                thumbnailUrl = it.imageUrl
            )
        }
}
