package dev.thiagosouto.marvelpoc.data.mappers

import dev.thiagosouto.marvelpoc.data.Comics

/**
 * Mapper to comics domain to ui model
 */
class ComicsMapper {

    /**
     * Mapper to comics domain to ui model
     */
    fun apply(comics: List<Comics>): List<dev.thiagosouto.marvelpoc.data.model.view.Comics> =
        comics.map {
            dev.thiagosouto.marvelpoc.data.model.view.Comics(
                title = it.title,
                thumbnailUrl = it.imageUrl
            )
        }
}
