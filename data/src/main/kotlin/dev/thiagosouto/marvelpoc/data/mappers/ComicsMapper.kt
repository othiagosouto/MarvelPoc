package dev.thiagosouto.marvelpoc.data.mappers

import dev.thiagosouto.marvelpoc.data.Comics

class ComicsMapper {
    fun apply(comics: List<Comics>): List<dev.thiagosouto.marvelpoc.data.model.view.Comics> =
        comics.map {
            dev.thiagosouto.marvelpoc.data.model.view.Comics(
                title = it.title,
                thumbnailUrl = it.imageUrl
            )
        }
}