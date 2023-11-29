package dev.thiagosouto.marvelpoc.data.mappers

import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.shared.Mapper

/**
 * Mapper to comics domain to ui model
 */
class ComicsMapper: Mapper<List<Comics>, List<dev.thiagosouto.marvelpoc.data.model.view.Comics>> {

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
