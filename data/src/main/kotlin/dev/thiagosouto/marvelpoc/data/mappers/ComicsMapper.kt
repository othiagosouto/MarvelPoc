package dev.thiagosouto.marvelpoc.data.mappers

import dev.thiagosouto.domain.Mapper
import dev.thiagosouto.domain.MapperList
import dev.thiagosouto.marvelpoc.data.Comics

/**
 * Mapper to comics domain to ui model
 */
class ComicsMapper: MapperList<Comics, dev.thiagosouto.marvelpoc.data.model.view.Comics> {

    /**
     * Mapper to comics domain to ui model
     */
    override fun apply(input: List<Comics>): List<dev.thiagosouto.marvelpoc.data.model.view.Comics> =
        input.map {
            dev.thiagosouto.marvelpoc.data.model.view.Comics(
                title = it.title,
                thumbnailUrl = it.imageUrl
            )
        }
}
